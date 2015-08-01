package pivotal.io.batch.domain;

/**
 * Created by kimm5 on 8/1/15.
 */
public enum  POS {
    Trigger(31), RST(30), BG1(29), BG0(28), CS1(27), CS0(26), CKE1(25), CKE0(24),
    OST1(23),ODT0(22), A16(21), A15(20), A14(19), A17(18), ACTN(17), A13(16), A12(15),
    A11(14), A10(13), A9(12), A8(11), A7(10), A6(9), A5(8), A4(7), A3(6), A2(5),A1(4), A0(3),
    C2(2), BA1(1),BA0(0);

    int position;

    POS(int pos){
        position=pos;
    }

    public int getPosition(){
        return position;
    }

}
