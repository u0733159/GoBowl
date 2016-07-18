package edu.gatech.seclass.gobowl;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private Context theContext;
    public ToastUtil(Context context)
    {
        theContext = context;
    }

    public Toast toast_msg(String msg)
    {
        return toast_msg(msg, Toast.LENGTH_LONG);
    }

    public Toast toast_msg(String msg, int duration)
    {
        Toast t = Toast.makeText(theContext, msg, duration);
        t.show();
        return t;
    }
}
