package org.fbi.posprize.domain;

import org.fbi.posprize.domain.base.TOA;
import org.fbi.posprize.domain.base.TOABody;
import org.fbi.posprize.domain.base.TOAHeader;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-15
 * Time: 下午5:54
 * To change this template use File | Settings | File Templates.
 */
public class TOA1001 extends TOA implements Serializable {
    private Header header = new Header();
    private Body body = new Body();

    @Override
    public TOAHeader getHeader() {
        return header;
    }

    @Override
    public TOABody getBody() {
        return body;
    }

    //====================================================================
    private static class Header extends TOAHeader {
    }

    private static class Body extends TOABody {
        private String cardEventId;
        private String cardEventName;

        private String getCardEventId() {
            return cardEventId;
        }

        private void setCardEventId(String cardEventId) {
            this.cardEventId = cardEventId;
        }

        private String getCardEventName() {
            return cardEventName;
        }

        private void setCardEventName(String cardEventName) {
            this.cardEventName = cardEventName;
        }
    }

}
