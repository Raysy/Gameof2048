package raysi.game2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvScore;
    public static MainActivity mainActivity=null;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = 0;
        tvScore = (TextView) findViewById(R.id.score_tv);
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public MainActivity(){
        mainActivity = this;
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score+"");
    }

    public void addScore(int s){
        score+=s;
        showScore();
    }
}
