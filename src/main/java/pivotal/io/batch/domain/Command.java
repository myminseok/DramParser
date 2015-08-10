package pivotal.io.batch.domain;

public abstract class Command {

    type name;
    byte bytes[] = new byte[]{0,0,0,0};
    public  enum type{
        UND, MRS, DES, REF, PRE, RFU,PDX, ACT, PDE, WR, RD, ZQC, NOP ;

    }

    /**
     *  bit binary index order :31... 0
     */
    public static enum  POS {
        Trigger(31), RST(30), BG1(29), BG0(28), CS1(27), CS0(26), CKE1(25), CKE0(24),
        ODT1(23),ODT0(22), A16(21), A15(20), A14(19), A17(18), ACTN(17), A13(16), A12(15),
        A11(14), A10(13), A9(12), A8(11), A7(10), A6(9), A5(8), A4(7), A3(6), A2(5),A1(4), A0(3),
        C2(2), BA1(1),BA0(0);

        int position;

        POS(int pos){
            position=pos;
        }

        /**
         * 31 ... 0
         * @return
         */
        public int getHumanIndex(){
            return position;
        }

        /**
         * 0 ...31
         * @return
         */
        public int getBinaryIndex(){
            return 31-position;
        }

    }

//    abstract void initStateTransitonMap();
    public abstract boolean isMatching(byte[] v);


    public Command(){
//        initStateTransitonMap();
    }

    public Command(type name){
        this.name=name;
//        initStateTransitonMap();
    }

    public void setBytes(byte[] v){
        this.bytes = v;
    }


    public type getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setTrigger(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }
    public void setRST(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }
    public void setBG1(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }
    public void setBG0(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }
    /**
     * @deprecated
     * @param value
     */
    public void setCS1(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }
    public void setCS0(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }

    /**
     * @deprecated
     * @param value
     */
    public void setCKE1(int value){
        setBit(bytes,POS.Trigger.getBinaryIndex(),value);
    }
    public void setCKE0(int value){
        setBit(bytes,POS.CKE0.getBinaryIndex(),value);
    }
    public void setODT1(int value){
        setBit(bytes,POS.ODT1.getBinaryIndex(),value);
    }
    public void setODT0(int value){
        setBit(bytes,POS.ODT0.getBinaryIndex(),value);
    }
    public void setA16(int value){setBit(bytes,POS.A16.getBinaryIndex(),value);}
    public void setA15(int value){setBit(bytes,POS.A15.getBinaryIndex(),value);}
    public void setA14(int value){
        setBit(bytes,POS.A14.getBinaryIndex(),value);
    }
    public void setA17(int value){
        setBit(bytes,POS.A17.getBinaryIndex(),value);
    }
    public void setACTN(int value){
        setBit(bytes,POS.ACTN.getBinaryIndex(),value);
    }
    public void setA13(int value){
        setBit(bytes,POS.A13.getBinaryIndex(),value);
    }
    public void setA12(int value){
        setBit(bytes,POS.A12.getBinaryIndex(),value);
    }
    public void setA11(int value){
        setBit(bytes,POS.A11.getBinaryIndex(),value);
    }
    public void setA10(int value){
        setBit(bytes,POS.A10.getBinaryIndex(),value);
    }
    public void setA9(int value){
        setBit(bytes,POS.A9.getBinaryIndex(),value);
    }
    public void setA8(int value){
        setBit(bytes,POS.A8.getBinaryIndex(),value);
    }
    public void setA7(int value){
        setBit(bytes,POS.A7.getBinaryIndex(),value);
    }
    public void setA6(int value){
        setBit(bytes,POS.A6.getBinaryIndex(),value);
    }
    public void setA5(int value){
        setBit(bytes,POS.A5.getBinaryIndex(),value);
    }
    public void setA4(int value){
        setBit(bytes,POS.A4.getBinaryIndex(),value);
    }
    public void setA3(int value){
        setBit(bytes,POS.A3.getBinaryIndex(),value);
    }
    public void setA2(int value){
        setBit(bytes,POS.A2.getBinaryIndex(),value);
    }
    public void setA1(int value){
        setBit(bytes,POS.A1.getBinaryIndex(),value);
    }
    public void setA0(int value){
        setBit(bytes,POS.A0.getBinaryIndex(),value);
    }
    public void setC2(int value){
        setBit(bytes,POS.C2.getBinaryIndex(),value);
    }
    public void setBA1(int value){
        setBit(bytes,POS.BA1.getBinaryIndex(),value);
    }
    public void setBA0(int value){
        setBit(bytes,POS.BA0.getBinaryIndex(),value);
    }


    public int getBit(int pos){
        return Command.getBit(this.bytes,pos);
    }

    /**
     * 0....31 order
     *
     * @param data
     * @param pos
     */
    public static int getBit(byte[] data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }

    /**
     * 0....31 order
     *
     * @param data
     * @param pos
     * @param val
     */
    public static void setBit(byte[] data, int pos, int val) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte oldByte = data[posByte];
        oldByte = (byte) (((0xFF7F>>posBit) & oldByte) & 0x00FF);
        byte newByte = (byte) ((val<<(8-(posBit+1))) | oldByte);
        data[posByte] = newByte;
    }



    public static String getRealBinary(byte[] input){
        StringBuilder sb = new StringBuilder();

        for (byte c : input) {
            for (int n =  128; n > 0; n >>= 1){
                if ((c & n) == 0)
                    sb.append('0');
                else sb.append('1');
            }
        }

        return sb.toString();
    }



    /**
     * 자바에서 사용하는 Big-Endian 정수값을 Little-Endian 바이트 배열로 변환한다.
     */
    public static byte[] getBigEndian(byte[] v){
        byte[] buf = new byte[4];
        buf[3] = v[0];
        buf[2] = v[1];
        buf[1] = v[2];
        buf[0] = v[3];
        return buf;
    }

    /**
     * 자바에서 사용하는 Big-Endian 정수값을 Little-Endian 바이트 배열로 변환한다.
     */
    public static byte[] getLittleEndian(int v){
        byte[] buf = new byte[4];
        buf[3] = (byte)((v >>> 24) & 0xFF);
        buf[2] = (byte)((v >>> 16) & 0xFF);
        buf[1] = (byte)((v >>> 8) & 0xFF);
        buf[0] = (byte)((v >>> 0) & 0xFF);
        return buf;
    }


    public static String byteToBits(byte[]  v){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%8s", Integer.toBinaryString(v[0] & 0xFF)).replace(' ','0'));
        sb.append("-");
        sb.append(String.format("%8s", Integer.toBinaryString(v[1] & 0xFF)).replace(' ', '0'));
        sb.append("-");
        sb.append(String.format("%8s", Integer.toBinaryString(v[2] & 0xFF)).replace(' ', '0'));
        sb.append("-");
        sb.append(String.format("%8s", Integer.toBinaryString(v[3] & 0xFF)).replace(' ', '0'));
        return sb.toString();
    }


    public static String parse(byte[] v){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%8s-%s:%2s, ", POS.CKE0.name(),POS.CKE0.getHumanIndex(), Command.getBit(v,POS.CKE0.getBinaryIndex())));
        sb.append(String.format("%8s-%s:%2s, ", POS.CS0.name(), POS.CS0.getHumanIndex(), Command.getBit(v, POS.CS0.getBinaryIndex())));
        sb.append(String.format("%8s-%s:%2s, ", POS.ACTN.name(),POS.ACTN.getHumanIndex(),  Command.getBit(v, POS.ACTN.getBinaryIndex())));
        sb.append(String.format("%8s-%s:%2s, ", POS.A16.name(), POS.A16.getHumanIndex(), Command.getBit(v, POS.A16.getBinaryIndex())));
        sb.append(String.format("%8s-%s:%2s, ", POS.A15.name(), POS.A15.getHumanIndex(), Command.getBit(v, POS.A15.getBinaryIndex())));
        sb.append(String.format("%8s-%s:%2s, ", POS.A14.name(), POS.A14.getHumanIndex(), Command.getBit(v, POS.A14.getBinaryIndex())));
        return sb.toString();

    }


    public String byteToHexs(){
        return bytesToHex(this.bytes);
    }

    public String byteToBits(){
        return byteToBits(this.bytes);
    }
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(", ");
        sb.append(parse(bytes) );
        sb.append(", ");
        sb.append(byteToBits(bytes));
        return String.format("%s %s %8s ",byteToBits(bytes), this.name, parse(bytes) );
    }


}
