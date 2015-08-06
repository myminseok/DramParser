package pivotal.io.batch.domain;

public abstract class Command {

    type name;
    byte bytes[] = new byte[]{0,0,0,0};
//    Map<State.type, State.type> stateTransitionMap = new HashMap<State.type, State.type>();
    public  enum type{
        UND, MRS, DES, REF, PRE, RFU,PDX, ACT, PDE, WR, RD, ZQC, NOP ;

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
        setBit(bytes,31,value);
    }
    public void setRST(int value){
        setBit(bytes,30,value);
    }
    public void setBG1(int value){
        setBit(bytes,29,value);
    }
    public void setBG0(int value){
        setBit(bytes,28,value);
    }
    /**
     * @deprecated
     * @param value
     */
    public void setCS1(int value){
        setBit(bytes,27,value);
    }
    public void setCS0(int value){
        setBit(bytes,26,value);
    }

    /**
     * @deprecated
     * @param value
     */
    public void setCKE1(int value){
        setBit(bytes,25,value);
    }
    public void setCKE0(int value){
        setBit(bytes,24,value);
    }
    public void setODT1(int value){
        setBit(bytes,23,value);
    }
    public void setODT0(int value){
        setBit(bytes,22,value);
    }
    public void setA16(int value){
        setBit(bytes,21,value);
    }
    public void setA15(int value){
        setBit(bytes,20,value);
    }
    public void setA14(int value){
        setBit(bytes,19,value);
    }
    public void setA17(int value){
        setBit(bytes,18,value);
    }
    public void setACTN(int value){
        setBit(bytes,17,value);
    }
    public void setA13(int value){
        setBit(bytes,16,value);
    }
    public void setA12(int value){
        setBit(bytes,15,value);
    }
    public void setA11(int value){
        setBit(bytes,14,value);
    }
    public void setA10(int value){
        setBit(bytes,13,value);
    }
    public void setA9(int value){
        setBit(bytes,12,value);
    }
    public void setA8(int value){
        setBit(bytes,11,value);
    }
    public void setA7(int value){
        setBit(bytes,10,value);
    }
    public void setA6(int value){
        setBit(bytes,9,value);
    }
    public void setA5(int value){
        setBit(bytes,8,value);
    }
    public void setA4(int value){
        setBit(bytes,7,value);
    }
    public void setA3(int value){
        setBit(bytes,6,value);
    }
    public void setA2(int value){
        setBit(bytes,5,value);
    }
    public void setA1(int value){
        setBit(bytes,4,value);
    }
    public void setA0(int value){
        setBit(bytes,3,value);
    }
    public void setC2(int value){
        setBit(bytes,2,value);
    }
    public void setBA1(int value){
        setBit(bytes,1,value);
    }
    public void setBA0(int value){
        setBit(bytes,0,value);
    }


    public int getBit(int pos){
        return Command.getBit(this.bytes,pos);
    }

    public static int getBit(byte[] data, int pos) {
        int posByte = pos/8;
        int posBit = pos%8;
        byte valByte = data[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }

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
        sb.append(POS.CKE0.name()).append(":").append(Command.getBit(v,POS.CKE0.getPosition())).append(", ");
        sb.append(POS.CS0.name()).append(":").append(Command.getBit(v,POS.CS0.getPosition())).append(", ");
        sb.append(POS.ACTN.name()).append(":").append(Command.getBit(v,POS.ACTN.getPosition())).append(", ");
        sb.append(POS.A16.name()).append(":").append(Command.getBit(v,POS.A16.getPosition())).append(", ");
        sb.append(POS.A15.name()).append(":").append(Command.getBit(v,POS.A15.getPosition())).append(", ");
        sb.append(POS.A14.name()).append(":").append(Command.getBit(v, POS.A14.getPosition()));
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
        return sb.toString();
    }



//    public static void main(String[] args) {
//
//
//        byte bytes[] = new byte[]{0,0,0,0};
//        String sbytes = getRealBinary(bytes);
//        System.out.println(sbytes);
//        System.out.println(byteToBits(bytes));
//
//
//        for(int i = 0; i <= 31;i++)
//            setBit(bytes, i,1);
//
//        for(int i=0;i<=31;i++)
//            System.out.print(getBit(bytes,i));
//
//    }


}
