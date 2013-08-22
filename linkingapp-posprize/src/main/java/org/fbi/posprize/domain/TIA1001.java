package org.fbi.posprize.domain;


import org.fbi.posprize.domain.base.TIA;
import org.fbi.posprize.domain.base.TIABody;
import org.fbi.posprize.domain.base.TIAHeader;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-15
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
public class TIA1001 extends TIA implements Serializable {
    private  Header header = new Header();
    private  Body body = new Body();

    public TIAHeader getHeader() {
        return  header;
    }

    public TIABody getBody() {
        return  body;
    }

    //====================================================================
    private  static class Header extends TIAHeader {
    }

    private static class Body extends TIABody {
    }
}
