package com.arcsoft.Home;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.R;
import com.arcsoft.face_talk.VideoChatViewActivity;
import com.arcsoft.sdk_demo.DetecterActivity;
import com.arcsoft.sdk_demo.FT311GPIOInterface;
import com.arcsoft.sdk_demo.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView edit1;//输入框
    private ImageView clear;//清除按钮
    private ImageView one;//按钮1
    private ImageView two;//按钮2
    private ImageView three;//按钮3
    private ImageView four;//按钮4
    private ImageView five;//按钮5
    private ImageView six;//按钮6
    private ImageView seven;//按钮7
    private ImageView eghit;//按钮8
    private ImageView nine;//按钮9
    private ImageView ten;//按钮7
    private ImageView zero;//按钮8
    private ImageView tenty;//按钮9
    private ImageView bt_psw;//按钮密码解锁
    private ImageView bt_phone;//按钮视频通话
    private ImageView bt_face;//按钮人脸识别
    public FT311GPIOInterface gpiointerface;
    private static final int REQUEST_CODE_OP = 3;

    public byte outMap; /*outmap*/
    public byte inMap; /*inmap*/
    public byte outData; /*output data*/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_home);
        edit1= (TextView) this.findViewById(R.id.edit1);
        clear= (ImageView) this.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        one= (ImageView) this.findViewById(R.id.one);
        one.setOnClickListener(this);
        two= (ImageView) this.findViewById(R.id.two);
        two.setOnClickListener(this);
        three= (ImageView) this.findViewById(R.id.three);
        three.setOnClickListener(this);
        four= (ImageView) this.findViewById(R.id.four);
        four.setOnClickListener(this);
        five= (ImageView) this.findViewById(R.id.five);
        five.setOnClickListener(this);
        six= (ImageView) this.findViewById(R.id.six);
        six.setOnClickListener(this);
        seven= (ImageView) this.findViewById(R.id.seven);
        seven.setOnClickListener(this);
        eghit= (ImageView) this.findViewById(R.id.eghit);
        eghit.setOnClickListener(this);
        nine= (ImageView) this.findViewById(R.id.nine);
        nine.setOnClickListener(this);
        ten= (ImageView) this.findViewById(R.id.ten);
        ten.setOnClickListener(this);
        tenty= (ImageView) this.findViewById(R.id.tenty);
        tenty.setOnClickListener(this);
        zero= (ImageView) this.findViewById(R.id.zero);
        zero.setOnClickListener(this);
        bt_face= (ImageView) this.findViewById(R.id.bt_face);
        bt_face.setOnClickListener(this);
        bt_phone= (ImageView) this.findViewById(R.id.bt_phone);
        bt_phone.setOnClickListener(this);
        bt_psw= (ImageView) this.findViewById(R.id.bt_psw);
        bt_psw.setOnClickListener(this);
        gpiointerface = new FT311GPIOInterface(this);
        resetFT311();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
            String textmsg=edit1.getText().toString();
        switch (view.getId()){
            case R.id.one:
                  textmsg+="1";
                  edit1.setText(textmsg);
                  break;
            case R.id.two:
                textmsg+="2";
                edit1.setText(textmsg);
                break;
            case R.id.three:
                textmsg+="3";
                edit1.setText(textmsg);
                break;
            case R.id.four:
                textmsg+="4";
                edit1.setText(textmsg);
                break;
            case R.id.five:
                textmsg+="5";
                edit1.setText(textmsg);
                break;
            case R.id.six:
                textmsg+="6";
                edit1.setText(textmsg);
                break;
            case R.id.seven:
                textmsg+="7";
                edit1.setText(textmsg);
                break;
            case R.id.eghit:
                textmsg+="8";
                edit1.setText(textmsg);
                break;
            case R.id.nine:
                textmsg+="9";
                edit1.setText(textmsg);
                break;
            case R.id.ten:
                textmsg+="#";
                edit1.setText(textmsg);
                break;
            case R.id.zero:
                textmsg+="0";
                edit1.setText(textmsg);
                break;
            case R.id.tenty:
                textmsg+="*";
                edit1.setText(textmsg);
                break;
            case R.id.clear:
                edit1.setText("");
                break;
            case R.id.bt_face:
                startDetector(0);
                break;
            case R.id.bt_phone:
                intent = new Intent(this, VideoChatViewActivity.class);
                startActivity(intent);
                gpiointerface.DestroyAccessory();
                break;
            case R.id.bt_psw:
                if ("123456".equals(textmsg)){
                    Toast.makeText(this, "密码输入正确，正在开门！", Toast.LENGTH_SHORT).show();
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                configFT311();
                                setOutDataMsg();
                                writeOutDataMsg();
                            }
                        }).start();
                    }catch (Exception e){
                    }
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            configFT311();
                        }
                    }, 4000);//这里停留时间为1000=1s。
                }else {
                    edit1.setText("");
                    Toast.makeText(this, "密码输入不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    protected void resetFT311() {
        gpiointerface.ResetPort();
        ProcessReadData((byte)0);
        outMap = (byte) 0x80;
        inMap = (byte) 0x7f;
        Toast.makeText(this, "初始化中！", Toast.LENGTH_SHORT).show();
    }

    protected void configFT311(){
        outMap &= ~(1<<0);
        outMap |= (1<<0);
        inMap &= ~(1<<0);
        outData &= ~outMap;
        outData &= ~outMap;
        gpiointerface.ConfigPort(outMap, inMap);
    }

    protected void setOutDataMsg(){
        outData &= ~(1<<0);
        outData |= (1<<0);
    }

    protected void writeOutDataMsg(){
        outData &= outMap;
        gpiointerface.WritePort(outData);
    }
    @Override
    protected void onResume() {
        // Ideally should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        gpiointerface.ResumeAccessory();
    }

    @Override
    protected void onPause() {
        // Ideally should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        gpiointerface.DestroyAccessory();
        super.onDestroy();
    }
    public void ProcessReadData(byte portData)
    {
        byte cmddata;
        cmddata = portData;
		/*check if the command is write*/
		/*just process input map*/
        cmddata &= inMap;
		/*read data is to update the LEDs*/
    }
    private void startDetector(int camera) {
        Intent it = new Intent(HomeActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
        gpiointerface.DestroyAccessory();
    }
}
