package code.rms1;

import code.rms2.requests.ObjMessage;

public interface r1Service {

    String message(String x);
    String obj(ObjMessage messageObject);

}
