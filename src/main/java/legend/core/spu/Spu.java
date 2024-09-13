package legend.core.spu;

import legend.core.MathHelper;
import legend.core.audio.GenericSource;
import legend.game.sound.ReverbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import static legend.core.GameEngine.AUDIO_THREAD;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;

import java.util.ArrayList;
import java.util.List;

public class Spu {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Spu.class);
  private static final Marker SPU_MARKER = MarkerManager.getMarker("SPU");

  private static final int SOUND_TPS = 60;
  private static final int SAMPLES_PER_TICK = 44_100 / SOUND_TPS;

  private GenericSource source;

  private final short[] spuOutput = new short[SAMPLES_PER_TICK * 2];
  public byte[] ram = new byte[0x8_0000]; //  spu 0x8_0000 

  private final float[] reverbWorkArea = new float[0x4_0000];
  public final Voice[] voices = new Voice[24];

  private int mainVolumeL;
  private int mainVolumeR;
  private int reverbOutputVolumeL;
  private int reverbOutputVolumeR;
  private long keyOn;
  private long keyOff;
  private long channelFmMode;
  private long channelNoiseMode;
  private long channelReverbMode;
  private int reverbCurrentAddress;
  private final Control control = new Control();
  private boolean reverbEnabled;
  private boolean muted = true;
  private int noiseFrequencyShift;
  private int noiseFrequencyStep;
  private final Reverb reverb = new Reverb();




  public List<Integer> attackSoundsBentSlots = new ArrayList<>();
  public List<Integer> attackSoundsBentOffsets = new ArrayList<>();
  public List<Integer> attackSpuRamSizes = new ArrayList<>();

  public List<Integer> effectSoundsBentSlots = new ArrayList<>();
  public List<Integer> effectSoundsBentOffsets = new ArrayList<>();
  public List<Integer> effectSpuRamSizes = new ArrayList<>();

  public Spu() {
    for(int i = 0; i < this.voices.length; i++) {
      this.voices[i] = new Voice(i);
    }

    // Initialize silent loop, voices start pointing to this
    for(int i = 0x1010; i < 0x1020; i++) {
      this.ram[i] = 0x7;
    }

    for(int i = 0; i < interpolationWeights.length; i++) {
      final double pow1 = i / (double)interpolationWeights.length;
      final double pow2 = pow1 * pow1;
      final double pow3 = pow2 * pow1;

      interpolationWeights[i] = new double[] {
        0.45d * (-pow3 + 2 * pow2 - pow1),
        0.45d * (3 * pow3 - 5 * pow2 + 2),
        0.45d * (-3 * pow3 + 4 * pow2 + pow1),
        0.45d * (pow3 - pow2)
      };
    }

    for(int i = 0; i < sampleRates.length; i++) {
      sampleRates[i] = (int)Math.round(0x1000 * Math.pow(2, i / (double)sampleRates.length));
    }
  }

  public void init() {
    this.source = AUDIO_THREAD.addSource(new GenericSource(AL_FORMAT_STEREO16, 44100));
  }

  private short reverbL;
  private short reverbR;

  public void tick() {
    synchronized(Spu.class) {
      int dataIndex = 0;
      for(int i = 0; i < SAMPLES_PER_TICK; i++) {
        int sumLeft = 0;
        int sumRight = 0;

        int sumLeftReverb = 0;
        int sumRightReverb = 0;

        final long edgeKeyOn = this.keyOn;
        final long edgeKeyOff = this.keyOff;
        this.keyOn = 0;
        this.keyOff = 0;

        if(edgeKeyOn != 0) {
          LOGGER.debug(SPU_MARKER, "Keying on %x", edgeKeyOn);
        }

        if(edgeKeyOff != 0) {
          LOGGER.debug(SPU_MARKER, "Keying off %x", edgeKeyOff);
        }

        this.tickNoiseGenerator();

        for(int voiceIndex = 0; voiceIndex < this.voices.length; voiceIndex++) {
          final Voice v = this.voices[voiceIndex];

          //keyOn and KeyOff are edge triggered on 0 to 1
          if((edgeKeyOn & 0x1L << voiceIndex) != 0) {
            LOGGER.debug(SPU_MARKER, "Keying on voice %d", voiceIndex);
            v.keyOn();
          }

          if((edgeKeyOff & 0x1L << voiceIndex) != 0) {
            LOGGER.debug(SPU_MARKER, "Keying off voice %d", voiceIndex);
            v.keyOff();
          }

          if(v.adsrPhase == Phase.Off) {
            v.latest = 0;
            continue;
          }

          short sample;
          if((this.channelNoiseMode & 0x1L << voiceIndex) == 0) {
            sample = this.sampleVoice(voiceIndex);
          } else {
            //Generated by tickNoiseGenerator
            sample = (short)this.noiseLevel;
          }

          //Handle ADSR Envelope
          sample = (short)(sample * v.adsrVolume >> 15);
          v.tickAdsr();

          //Save sample for possible pitch modulation
          v.latest = sample;

          //Sum each voice sample
          if(!this.muted) {
            sumLeft += sample * v.processVolume(v.volumeLeft) >> 15;
            sumRight += sample * v.processVolume(v.volumeRight) >> 15;
          }

          if((this.channelReverbMode & 0x1L << voiceIndex) != 0) {
            sumLeftReverb += sample * v.processVolume(v.volumeLeft) >> 15;
            sumRightReverb += sample * v.processVolume(v.volumeRight) >> 15;
          }
        }

        this.processReverb(sumLeftReverb, sumRightReverb);

        sumLeft += this.reverbL;
        sumRight += this.reverbR;

        //Clamp sum
        sumLeft = MathHelper.clamp(sumLeft, -0x8000, 0x7fff) * (short)this.mainVolumeL >> 15;
        sumRight = MathHelper.clamp(sumRight, -0x8000, 0x7fff) * (short)this.mainVolumeR >> 15;

        //Add to samples bytes to output list
        this.spuOutput[dataIndex++] = (short)sumLeft;
        this.spuOutput[dataIndex++] = (short)sumRight;
      }

      synchronized(this.source) {
        if(this.source.canBuffer()) {
          this.source.bufferOutput(this.spuOutput);
        }
      }
    }
  }

  public void processReverb(final int lInput, final int rInput) {
    final int dAPF1 = this.reverb.dAPF1;
    final int dAPF2 = this.reverb.dAPF2;
    final float vIIR = this.reverb.vIIR;
    final float vCOMB1 = this.reverb.vCOMB1;
    final float vCOMB2 = this.reverb.vCOMB2;
    final float vCOMB3 = this.reverb.vCOMB3;
    final float vCOMB4 = this.reverb.vCOMB4;
    final float vWALL = this.reverb.vWALL;
    final float vAPF1 = this.reverb.vAPF1;
    final float vAPF2 = this.reverb.vAPF2;
    final int mLSAME = this.reverb.mLSAME;
    final int mRSAME = this.reverb.mRSAME;
    final int mLCOMB1 = this.reverb.mLCOMB1;
    final int mRCOMB1 = this.reverb.mRCOMB1;
    final int mLCOMB2 = this.reverb.mLCOMB2;
    final int mRCOMB2 = this.reverb.mRCOMB2;
    final int dLSAME = this.reverb.dLSAME;
    final int dRSAME = this.reverb.dRSAME;
    final int mLDIFF = this.reverb.mLDIFF;
    final int mRDIFF = this.reverb.mRDIFF;
    final int mLCOMB3 = this.reverb.mLCOMB3;
    final int mRCOMB3 = this.reverb.mRCOMB3;
    final int mLCOMB4 = this.reverb.mLCOMB4;
    final int mRCOMB4 = this.reverb.mRCOMB4;
    final int dLDIFF = this.reverb.dLDIFF;
    final int dRDIFF = this.reverb.dRDIFF;
    final int mLAPF1 = this.reverb.mLAPF1;
    final int mRAPF1 = this.reverb.mRAPF1;
    final int mLAPF2 = this.reverb.mLAPF2;
    final int mRAPF2 = this.reverb.mRAPF2;
    final float vLIN = this.reverb.vLIN;
    final float vRIN = this.reverb.vRIN;

    // Input from mixer
    final float Lin = vLIN * (lInput / 32768.0f);
    final float Rin = vRIN * (rInput / 32768.0f);

    // Same side reflection L->L and R->R
    final float mlSame = this.saturateSample((Lin + this.loadReverb(dLSAME) * vWALL - this.loadReverb(mLSAME - 1)) * vIIR + this.loadReverb(mLSAME - 1));
    final float mrSame = this.saturateSample((Rin + this.loadReverb(dRSAME) * vWALL - this.loadReverb(mRSAME - 1)) * vIIR + this.loadReverb(mRSAME - 1));

    this.writeReverb(mLSAME, mlSame);
    this.writeReverb(mRSAME, mrSame);

    // Different side reflection L->R and R->L
    final float mlDiff = this.saturateSample((Lin + this.loadReverb(dRDIFF) * vWALL - this.loadReverb(mLDIFF - 1)) * vIIR + this.loadReverb(mLDIFF - 1));
    final float mrDiff = this.saturateSample((Rin + this.loadReverb(dLDIFF) * vWALL - this.loadReverb(mRDIFF - 1)) * vIIR + this.loadReverb(mRDIFF - 1));

    this.writeReverb(mLDIFF, mlDiff);
    this.writeReverb(mRDIFF, mrDiff);

    // Early echo (comb filter with input from buffer)
    float l = this.saturateSample(vCOMB1 * this.loadReverb(mLCOMB1) + vCOMB2 * this.loadReverb(mLCOMB2) + vCOMB3 * this.loadReverb(mLCOMB3) + vCOMB4 * this.loadReverb(mLCOMB4));
    float r = this.saturateSample(vCOMB1 * this.loadReverb(mRCOMB1) + vCOMB2 * this.loadReverb(mRCOMB2) + vCOMB3 * this.loadReverb(mRCOMB3) + vCOMB4 * this.loadReverb(mRCOMB4));

    // Late reverb APF1 (All pass filter 1 with input from COMB)
    l = this.saturateSample(l - this.saturateSample(vAPF1 * this.loadReverb(mLAPF1 - dAPF1)));
    r = this.saturateSample(r - this.saturateSample(vAPF1 * this.loadReverb(mRAPF1 - dAPF1)));

    this.writeReverb(mLAPF1, l);
    this.writeReverb(mRAPF1, r);

    l = this.saturateSample(l * vAPF1 + this.loadReverb(mLAPF1 - dAPF1));
    r = this.saturateSample(r * vAPF1 + this.loadReverb(mRAPF1 - dAPF1));

    // Late reverb APF2 (All pass filter 2 with input from APF1)
    l = this.saturateSample(l - this.saturateSample(vAPF2 * this.loadReverb(mLAPF2 - dAPF2)));
    r = this.saturateSample(r - this.saturateSample(vAPF2 * this.loadReverb(mRAPF2 - dAPF2)));

    this.writeReverb(mLAPF2, l);
    this.writeReverb(mRAPF2, r);

    l = this.saturateSample(l * vAPF2 + this.loadReverb(mLAPF2 - dAPF2));
    r = this.saturateSample(r * vAPF2 + this.loadReverb(mRAPF2 - dAPF2));

    // Output to mixer (output volume multiplied with input from APF2)
    l = this.saturateSample(l * (this.reverbOutputVolumeL / 32768.0f));
    r = this.saturateSample(r * (this.reverbOutputVolumeR / 32768.0f));

    // Saturate address
    this.reverbCurrentAddress = this.reverbCurrentAddress + 1 & (this.reverbWorkArea.length - 1);

    this.reverbL = (short)(l * 0x8000);
    this.reverbR = (short)(r * 0x8000);
  }

  public float saturateSample(final float sample) {
    return MathHelper.clamp(sample, -1.0f, 1.0f);
  }

  private void writeReverb(final int addr, final float value) {
    if(!this.reverbEnabled) {
      return;
    }

    final int address = this.reverbCurrentAddress + addr & (this.reverbWorkArea.length - 1);
    this.reverbWorkArea[address] = value;
  }

  private float loadReverb(final int addr) {
    final int address = this.reverbCurrentAddress + addr & (this.reverbWorkArea.length - 1);
    return this.reverbWorkArea[address];
  }

  //Wait(1 cycle); at 44.1kHz clock
  //Timer=Timer-NoiseStep  ;subtract Step(4..7)
  //ParityBit = NoiseLevel.Bit15 xor Bit12 xor Bit11 xor Bit10 xor 1
  //IF Timer<0 then NoiseLevel = NoiseLevel * 2 + ParityBit
  //IF Timer<0 then Timer = Timer + (20000h SHR NoiseShift); reload timer once
  //IF Timer<0 then Timer = Timer + (20000h SHR NoiseShift); reload again if needed
  int noiseTimer;
  int noiseLevel;

  private void tickNoiseGenerator() {
    final int noiseStep = this.control.noiseFrequencyStep() + 4;
    final int noiseShift = this.control.noiseFrequencyShift();

    this.noiseTimer -= noiseStep;
    final int parityBit = this.noiseLevel >> 15 & 0x1 ^ this.noiseLevel >> 12 & 0x1 ^ this.noiseLevel >> 11 & 0x1 ^ this.noiseLevel >> 10 & 0x1 ^ 1;
    if(this.noiseTimer < 0) {
      this.noiseLevel = this.noiseLevel * 2 + parityBit;
    }
    if(this.noiseTimer < 0) {
      this.noiseTimer += 0x20000 >> noiseShift;
    }
    if(this.noiseTimer < 0) {
      this.noiseTimer += 0x20000 >> noiseShift;
    }
  }

  private short sampleVoice(final int v) {
    final Voice voice = this.voices[v];

    //Decode samples if its empty / next block
    if(!voice.hasSamples) {
      voice.decodeSamples(this.ram);
      voice.hasSamples = true;

      final byte flags = this.voices[v].spuAdpcm[1];
      final boolean loopStart = (flags & 0x4) != 0;

      if(loopStart) {
        assert voice.currentAddress >= 0 : "Negative address";
        voice.adpcmRepeatAddress = voice.currentAddress;
      }
    }

    //Get indices for gauss interpolation
    final int interpolationIndex = voice.counter.interpolationIndex();
    final int sampleIndex = voice.counter.currentSampleIndex();

    //Interpolate latest samples
    //this is why the latest 3 samples from the last block are saved because if index is 0
    //any subtraction is gonna be oob of the current voice adpcm array

    final double[] weights = interpolationWeights[interpolationIndex];

    double interpolated;
    interpolated = weights[0] * voice.getSample(sampleIndex - 3);
    interpolated += weights[1] * voice.getSample(sampleIndex - 2);
    interpolated += weights[2] * voice.getSample(sampleIndex - 1);
    interpolated += weights[3] * voice.getSample(sampleIndex);

    //Pitch modulation: Starts at voice 1 as it needs the last voice
    int step = voice.pitch;
    if(v > 0 && (this.channelFmMode & 0x1L << v) != 0) {
      final int factor = this.voices[v - 1].latest + 0x8000; //From previous voice
      step = step * factor >> 15;
      step &= 0xffff;
    }
    if(step > 0x3fff) {
      step = 0x4000;
    }

    voice.counter.register += step;

    if(voice.counter.currentSampleIndex() >= 28) {
      //Beyond the current adpcm sample block prepare to decode next
      voice.counter.currentSampleIndex(voice.counter.currentSampleIndex() - 28);
      voice.currentAddress += 2;
      voice.hasSamples = false;

      //LoopEnd and LoopRepeat flags are set after the "current block" set them as it's finished
      final byte flags = this.voices[v].spuAdpcm[1];
      final boolean loopEnd = (flags & 0x1) != 0;
      final boolean loopRepeat = (flags & 0x2) != 0;

      if(loopEnd) {
        if(loopRepeat) {
          assert voice.adpcmRepeatAddress >= 0 : "Negative address";
          assert voice.adpcmRepeatAddress < this.ram.length : "Address overflow";
          voice.currentAddress = voice.adpcmRepeatAddress;
        } else {
          voice.adsrPhase = Phase.Off;
          voice.adsrVolume = 0;
        }
      }
    }

    return (short)interpolated;
  }

  public void directWrite(final int spuRamOffset, final byte[] dma) {
    LOGGER.info("Performing direct write from stack to SPU @ %04x (%d bytes)", spuRamOffset, dma.length);

    synchronized(Spu.class) {
      System.arraycopy(dma, 0, this.ram, spuRamOffset, dma.length);
    }
  }

  public void setMainVolume(final int left, final int right) {
    LOGGER.info(SPU_MARKER, "Setting SPU main volume to %04x, %04x", left, right);

    synchronized(Spu.class) {
      this.mainVolumeL = left;
      this.mainVolumeR = right;
    }
  }

  public int getMainVolumeLeft() {
    synchronized(Spu.class) {
      return this.mainVolumeL;
    }
  }

  public int getMainVolumeRight() {
    synchronized(Spu.class) {
      return this.mainVolumeR;
    }
  }

  public void setReverbVolume(final int left, final int right) {
    LOGGER.info(SPU_MARKER, "Setting SPU reverb volume to %04x, %04x", left, right);

    synchronized(Spu.class) {
      this.reverbOutputVolumeL = left;
      this.reverbOutputVolumeR = right;
    }
  }

  public void keyOff(final long voices) {
    LOGGER.debug(SPU_MARKER, "Setting SPU key off to %08x", voices);

    synchronized(Spu.class) {
      this.keyOff |= voices;
    }
  }

  public void keyOn(final long voices) {
    LOGGER.debug(SPU_MARKER, "Setting SPU key on to %08x", voices);

    synchronized(Spu.class) {
      this.keyOn |= voices;
    }
  }

  public void clearKeyOn() {
    LOGGER.info(SPU_MARKER, "Clearing SPU key on");

    synchronized(Spu.class) {
      this.keyOn = 0;
    }
  }

  public void setNoiseMode(final long noiseMode) {
//    LOGGER.debug(SPU_MARKER, "Setting SPU noise mode to %x", noiseMode);

    synchronized(Spu.class) {
      this.channelNoiseMode = noiseMode;
    }
  }

  public void setReverbMode(final long reverbMode) {
//    LOGGER.debug(SPU_MARKER, "Setting SPU reverb mode to %x", reverbMode);

    synchronized(Spu.class) {
      this.channelReverbMode = reverbMode;
    }
  }

  public void setReverb(final ReverbConfig reverb) {
    synchronized(Spu.class) {
      this.reverb.set(reverb);
    }
  }

  public void mute() {
    LOGGER.info(SPU_MARKER, "Muting SPU");

    synchronized(Spu.class) {
      this.muted = true;
    }
  }

  public void unmute() {
    LOGGER.info(SPU_MARKER, "Unmuting SPU");

    synchronized(Spu.class) {
      this.muted = false;
    }
  }

  public void enableReverb() {
    LOGGER.info(SPU_MARKER, "Enabling SPU reverb");

    synchronized(Spu.class) {
      this.reverbEnabled = true;
    }
  }

  public void disableReverb() {
    LOGGER.info(SPU_MARKER, "Disabling SPU reverb");

    synchronized(Spu.class) {
      this.reverbEnabled = false;
    }
  }

  public void setNoiseFrequency(final int packed) {
    LOGGER.info(SPU_MARKER, "Setting SPU noise frequency %x", packed);

    synchronized(Spu.class) {
      this.noiseFrequencyShift = packed >> 2 & 0xf;
      this.noiseFrequencyStep = packed & 0x3;
    }
  }

  private static final double[][] interpolationWeights = new double[512][];
  public static final int[] sampleRates = new int[768];
}
