package code;

import org.springframework.stereotype.Service;

@Service("r1service")
public class r1ServiceImpl implements r1Service {


    @Override
    public String message(String x) {
        return "??";
    }
}
