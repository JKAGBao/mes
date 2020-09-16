package project.bridgetek.com.applib.main.bean;


import com.paramsen.noise.Noise;

public class MessageEventWave {
    private float mCoef = 5.0F;
    private int mSampleNums = 256;
    private int mSampleRate = 1000;
    private float mSampleTmp;
    private int mSampleType = 1;
    private short[] msWave = new short[4096];
    private boolean isWatch = false;
    private float[] mFloats;

    public boolean isWatch() {
        return isWatch;
    }

    public MessageEventWave(byte[] RecvBytes, int RecvType, int RecvRate, int RecvNums, int RecvCoef, int RecvTemp) {
        this.mSampleType = RecvType;
        this.mSampleRate = RecvRate;
        this.mSampleNums = RecvNums;
        this.mSampleTmp = (RecvTemp / 100.0F);
        this.mCoef = Float.intBitsToFloat(RecvCoef);

        for (int i = 0; i < RecvNums; i++) {
            this.msWave[i] = ((short) (RecvBytes[(i * 2)] & 0xFF | RecvBytes[(i * 2 + 1)] << 8 & 0xFF00));
        }
    }

    public MessageEventWave(float[] list, int RecvType, int RecvRate, int RecvNums, int RecvCoef, int RecvTemp) {
        this.mSampleType = RecvType;
        this.mSampleRate = RecvRate;
        this.mSampleNums = RecvNums;
        this.mSampleTmp = (RecvTemp / 100.0F);
        this.mCoef = Float.intBitsToFloat(RecvCoef);
        isWatch = true;
        mFloats = list;
    }


    public boolean getFloatWave(float[] arrayOfWave, float[] arrayOfFft, float[] arrayOfValue) {
        float value = 0.0F;
        for (int i = 0; i < this.mSampleNums; i++) {
            arrayOfWave[i] = (this.mCoef * this.msWave[i]);
            value += arrayOfWave[i];
        }
        value /= this.mSampleNums;
        for (int i = 0; i < this.mSampleNums; i++) {
            arrayOfWave[i] -= value;
        }

        if (arrayOfValue != null) {
            arrayOfValue[0] = arrayOfWave[0];
            arrayOfValue[1] = 0;
            for (int i = 1; i < this.mSampleNums; i++) {
                float val = Math.abs(arrayOfWave[i]);
                if (arrayOfValue[0] < val) {
                    arrayOfValue[0] = val;
                }
            }
        }

        if (arrayOfFft != null) {

            float[] tmpfft;

            tmpfft = Noise.real().optimized().init(this.mSampleNums, true).fft(arrayOfWave);
            int j = arrayOfFft.length;

            for (int i = 0; i < j; i++) {
                float f1 = tmpfft[(i * 2)];
                float f2 = tmpfft[(i * 2 + 1)];
                arrayOfFft[i] = ((float) Math.sqrt(Math.pow(f1, 2.0D) + Math.pow(f2, 2.0D)) / j);
                float val = Math.abs(arrayOfFft[i]);
                if (arrayOfValue[1] < val) {
                    arrayOfValue[1] = val;
                }
            }
        }
        return true;
    }

    public boolean getFloat(float[] arrayOfWave, float[] arrayOfFft, float[] arrayOfValue) {
        float value = 0.0F;
        for (int i = 0; i < this.mSampleNums; i++) {
            arrayOfWave[i] = mFloats[i];
            value += arrayOfWave[i];
        }
        value /= this.mSampleNums;
        for (int i = 0; i < this.mSampleNums; i++) {
            arrayOfWave[i] -= value;
        }

        if (arrayOfValue != null) {
            arrayOfValue[0] = arrayOfWave[0];
            arrayOfValue[1] = 0;
            for (int i = 1; i < this.mSampleNums; i++) {
                float val = Math.abs(arrayOfWave[i]);
                if (arrayOfValue[0] < val) {
                    arrayOfValue[0] = val;
                }
            }
        }

        if (arrayOfFft != null) {

            float[] tmpfft;

            tmpfft = Noise.real().optimized().init(this.mSampleNums, true).fft(arrayOfWave);
            int j = arrayOfFft.length;

            for (int i = 0; i < j; i++) {
                float f1 = tmpfft[(i * 2)];
                float f2 = tmpfft[(i * 2 + 1)];
                arrayOfFft[i] = ((float) Math.sqrt(Math.pow(f1, 2.0D) + Math.pow(f2, 2.0D)) / j);
                float val = Math.abs(arrayOfFft[i]);
                if (arrayOfValue[1] < val) {
                    arrayOfValue[1] = val;
                }
            }
        }
        return true;
    }

    public int getSampleNums() {
        return this.mSampleNums;
    }

    public int getSampleRate() {
        return (this.mSampleRate << 8) / 100;
    }

    public int getSampleType() {
        return this.mSampleType;
    }

    public float getTemperature() {
        return this.mSampleTmp;
    }
}
