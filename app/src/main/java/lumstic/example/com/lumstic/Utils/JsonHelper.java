package lumstic.example.com.lumstic.Utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import lumstic.example.com.lumstic.R;

public class JsonHelper {

    Context context;
    public JsonHelper(Context context){
        this.context= context;
    }

    public void getStringFromJson(){
        try{
    InputStream inputStream = context.
            getResources().openRawResource(R.raw.json_data);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        Log.e("stringjson",new String(buffer));
}
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }}
