package project.bridgetek.com.applib.main.toos;

public class MessageEventValue {
    private float mCoef = 5.0F;
    private int mSampleNums = 256;
    private int mSampleRate = 1000;
    private float mSampleTmp;
    private int mSampleType = 1;
    private short[] msWave = new short[4096];

    public MessageEventValue(byte[] RecvBytes, int RecvType, int RecvRate, int RecvNums, int RecvCoef, int RecvTemp) {
        this.mSampleType = RecvType;
        this.mSampleRate = RecvRate;
        this.mSampleNums = RecvNums;
        this.mSampleTmp = (RecvTemp / 100.0F);
        this.mCoef = Float.intBitsToFloat(RecvCoef);
        int i = 0;
        while (i < RecvNums) {
            this.msWave[i] = ((short) (RecvBytes[(i * 2)] & 0xFF | RecvBytes[(i * 2 + 1)] << 8 & 0xFF00));
            i += 1;
        }
    }

    public float getPeakValue() {
        int j = Math.abs(this.msWave[0]);
        int i = 1;
        while (i < this.mSampleNums) {
            int k = j;
            if (j < Math.abs(this.msWave[i])) {
                k = Math.abs(this.msWave[i]);
            }
            i += 1;
            j = k;
        }
        return this.mCoef * j;
    }

    public float getRmsValue() {
        int j = 1;
        short[] arrayOfShort = this.msWave;
        int i = 0;
        if (this.mSampleNums == 0) {
            return 0;
        }
        while (i < this.mSampleNums) {
            j += arrayOfShort[i] * arrayOfShort[i];
            i += 1;
        }
        return this.mCoef * (float) Math.sqrt(j / this.mSampleNums);
    }

    public float getPeak() {
        short[] arrayOfShort = this.msWave;
        short a = arrayOfShort[0];
        short b = arrayOfShort[0];
        int i = 0;
        while (i < this.mSampleNums) {
            if (a < arrayOfShort[i]) {
                a = arrayOfShort[i];
            }
            if (b > arrayOfShort[i]) {
                b = arrayOfShort[i];
            }
            i += 1;
        }
        int abs = Math.abs(a);
        int abs1 = Math.abs(b);
        float c = (this.mCoef * abs) + (this.mCoef * abs1);
        return c;
    }

    public float getTemperature() {
        return this.mSampleTmp;
    }

    public float[] getValue() {
        short[] arrayOfShort = this.msWave;
        int j = Math.abs(arrayOfShort[0]);
        int i = 1;
        int k;
        while (i < this.mSampleNums) {
            k = j;
            if (j < Math.abs(arrayOfShort[i])) {
                k = Math.abs(arrayOfShort[i]);
            }
            i += 1;
            j = k;
        }
        float f2 = this.mCoef;
        float f3 = j;
        j = arrayOfShort[0];
        int m = arrayOfShort[0];
        i = 0;
        while (i < this.mSampleNums) {
            k = m;
            if (m < arrayOfShort[i]) {
                k = arrayOfShort[i];
            }
            int n = j;
            if (j > arrayOfShort[i]) {
                n = arrayOfShort[i];
            }
            i += 1;
            m = k;
            j = n;
        }
        float f4 = this.mCoef;
        float f5 = m - j;
        j = 0;
        i = 0;
        while (i < this.mSampleNums) {
            j += arrayOfShort[i];
            i += 1;
        }
        float f6 = this.mCoef * j / this.mSampleNums;
        j = 0;
        i = 0;
        while (i < this.mSampleNums) {
            j += Math.abs(arrayOfShort[i]);
            i += 1;
        }
        float f7 = this.mCoef * j / this.mSampleNums;
        float f1 = 0.0F;
        i = 0;
        while (i < this.mSampleNums) {
            f1 += (float) Math.sqrt(Math.abs(arrayOfShort[i]));
            i += 1;
        }
        f1 /= this.mSampleNums;
        j = 0;
        i = 0;
        while (i < this.mSampleNums) {
            j += arrayOfShort[i] * arrayOfShort[i];
            i += 1;
        }
        return new float[]{f2 * f3, f4 * f5, f6, f7, f1 * f1, this.mCoef * (float) Math.sqrt(j / this.mSampleNums)};
    }
}
