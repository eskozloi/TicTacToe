package com.example.tictactoe;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton buttons[] = new ImageButton[9];
    int[] btnClone = new int[9];
    String text = new String();
    int[][] bestMoves = new int[8][2];
    Integer bestMoveID = new Integer(-1);
    Integer movenmb = 0;
    //Integer here = 0;
    Boolean goLoose = false;
    Boolean wnr = false;
    Boolean modeNow = true;
    String P = "x";
    String E = "o";
    Integer Pn = -1;
    Random rand = new Random();
    int curentCV;
    final String PF = "x";
    final Integer PnF = -1;

    //Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        getSupportActionBar().hide();
        setContentView(R.layout.start_screen);
        curentCV=R.layout.start_screen;
        /*fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart (Animation animation){
            }
            public void onAnimationRepeat (Animation animation){
            }
            public void onAnimationEnd (Animation animation){
                setContentView(R.layout.game_field);
            }
        });*/
        /*ImageView  gifImageView = (ImageView) findViewById(R.id.gifplace);
        Glide.with(this)
                .load(R.drawable.tictactoegif)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(gifImageView);*/
    }

    @Override
    public void onClick(View v) {
        if(curentCV==R.layout.game_field && wnr == false && ((ImageButton) v).getContentDescription().toString() == "-"){
            for(int i = 0; i < buttons.length; i++){
                if(buttons[i].getContentDescription().toString() != "-"){break;}
                else if(i==8){P=PF;Pn=PnF;}
            }
            ((ImageButton) v).setContentDescription(P);
            ((ImageButton) v).setImageResource(takeImage(P,Integer.parseInt(((ImageButton) v).getTag().toString())));
            String idStr=getResources().getResourceEntryName(v.getId());
            btnClone[Integer.parseInt(idStr.substring(idStr.length()-1))-1] = Pn;
            movenmb+=1;
            //here=0;
            bestMoves = new int[9-movenmb][2];

            if(CheckWin(btnClone, true)){showWinner();}
            else if(CheckTie(btnClone)){showWinner();}
            else if(!modeNow){AITurn();}
            else{if(P=="x"){P="o";}else{P="x";}Pn=Pn*-1;}
        }
        else{
            switch (v.getId()) {
                case R.id.buttonStart:
                    //setContentView(R.layout.game_field);
                    //Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
                    //Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
                    startAnimation(true,true);
                    //v.startAnimation(fadeOut);
                    /*for(int i = 0; i < buttons.length; i++){
                        String btnId = "button" + (i + 1);
                        int resID = getResources().getIdentifier(btnId, "id", getPackageName());
                        buttons[i] = (Button) findViewById(resID);
                        buttons[i].setText("");
                        buttons[i].setOnClickListener(this);

                        btnClone[i] = 0;
                    }*/
                    break;
                case R.id.buttonLeft: case R.id.buttonRight:
                    modeNow=!modeNow;
                    TextView modeTxt = (TextView) findViewById(R.id.mode);
                    if(modeNow){modeTxt.setText("With Friend");}else{modeTxt.setText("With Computer");}
                    break;
                case R.id.goback:
                    startAnimation(false,true);
                    //startGif(false);
                    //setPar(2,2);
                    break;
                case R.id.leave:
                    finish();
                    System.exit(0);
                    break;
                case R.id.restart:
                    setPar(2,2);
                    break;
                default:
                    return;
            }
        }

    }
    public boolean CheckWin(int[] btnClone, boolean fin){
        // Horizontal
        for (int i = 0; i < 3; i++){
            if(btnClone[0+3*i] != 0 && btnClone[0+3*i] == btnClone[1+3*i] && btnClone[1+3*i] == btnClone[2+3*i]){
                if(fin){
                    String result = ((ImageButton) buttons[0+3*i]).getContentDescription().toString();
                    text = "Winner "+result;
                }
                return true;
            }
        }
        // Vertical
        for (int i = 0; i < 3; i++){
            if(btnClone[0+i] != 0 && btnClone[0+i] == btnClone[3+i] && btnClone[3+i] == btnClone[6+i]){
                if(fin){
                    String result = ((ImageButton) buttons[0+i]).getContentDescription().toString();
                    text = "Winner "+result;
                }
                return true;
            }
        }
        // Diagonal
        if(btnClone[0] != 0 && btnClone[0] == btnClone[4] && btnClone[4] == btnClone[8]){
            if(fin){
                String result = ((ImageButton) buttons[0]).getContentDescription().toString();
                text = "Winner "+result;
            }
            return true;
        }
        if(btnClone[6] != 0 && btnClone[6] == btnClone[4] && btnClone[4] == btnClone[2]){
            if(fin){
                String result = ((ImageButton) buttons[6]).getContentDescription().toString();
                text = "Winner "+result;
            }
            return true;
        }
        return false;
    }
    public boolean CheckTie(int[] btnClone){
        for (int i = 0; i < btnClone.length; i++){
            if(btnClone[i]==0){return false;}
            else if (i==8){
                text = "Tie";
                return true;
            }
        }
        return false;
    }
    public void showWinner(){
        TextView win = (TextView) findViewById(R.id.winner);
        win.setText(text);
        wnr=true;
    }
    public Integer searchBest(int[] Map, Integer Turn, Integer depth){
        if(CheckWin(Map, false)){return Turn*-1;}
        else if(CheckTie(Map)){return 0;}
        int[][] moves = new int[9+depth-movenmb][2];
        Integer pthcntr = 0;
        for (int i = 0; i < 9; i++) {
            if (Map[i] == 0) {
                Map[i] = Turn;
                Integer result = searchBest(Map, Turn*-1, depth-1);
                Map[i]=0;
                moves[pthcntr][0] = i;
                moves[pthcntr][1] += result;
                pthcntr+=1;
            }
        }
        Integer bestMove = new Integer(-1);
        Integer bestPlace = new Integer(-1);
        if(Turn==1){
            Integer bestScore = new Integer(-100000);
            for(int i=0; i<moves.length; i++){
                if(bestScore < moves[i][1]){
                    bestScore=moves[i][1];
                    bestMove=moves[i][0];
                    bestPlace=i;
                }
            }
        }
        else{
            Integer bestScore = new Integer(100000);
            for(int i=0; i<moves.length; i++){
                if(bestScore > moves[i][1]){
                    bestScore=moves[i][1];
                    bestMove=moves[i][0];
                    bestPlace=i;
                }
            }
        }
        if(depth==0){
            bestMoves=moves;
            bestMoveID=bestMove;
        }
        return moves[bestPlace][1];
    }
    public void deal(){
        Log.i("here","deal");
    }
    public void AITurn(){
        searchBest(btnClone, 1, 0);
        Log.i("here3", Arrays.deepToString(bestMoves));
        buttons[bestMoveID].setContentDescription(E);
        buttons[bestMoveID].setImageResource(takeImage(E, bestMoveID));
        btnClone[bestMoveID]=1;
        movenmb+=1;
        if(CheckWin(btnClone, true)){showWinner();}
        else if(CheckTie(btnClone)){showWinner();}
        else if(goLoose){deal();}
    }

    private int takeImage(String trn, int id) {
        String dir = "@drawable/";
        dir+=trn;
        dir+="1";
        dir+=rand.nextInt(3)+1;
        dir+=id+1;

        int imageResource = getResources().getIdentifier(dir, null, getPackageName());
        return imageResource;
    }

    private void startAnimation(boolean goNext, boolean start) {
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        if(start) {
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    startGif(goNext);
                }
            });
        }
        if(goNext){
            Button startbtn = (Button) findViewById(R.id.buttonStart);
            ImageView tictactoeTitle = (ImageView) findViewById(R.id.tictactoeTitle);
            TextView mode = (TextView) findViewById(R.id.mode);
            Button buttonRight = (Button) findViewById(R.id.buttonRight);
            Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
            Button leave = (Button) findViewById(R.id.leave);
            ImageView tictactoexo = (ImageView) findViewById(R.id.tictactoexo);

            if(start) {
                startbtn.setEnabled(false);
                buttonRight.setEnabled(false);
                buttonLeft.setEnabled(false);
                leave.setEnabled(false);

                startbtn.startAnimation(fadeOut);
                tictactoeTitle.startAnimation(fadeOut);
                mode.startAnimation(fadeOut);
                buttonRight.startAnimation(fadeOut);
                buttonLeft.startAnimation(fadeOut);
                tictactoexo.startAnimation(fadeOut);
                leave.startAnimation(fadeOut);
            }
            else{
                startbtn.startAnimation(fadeIn);
                tictactoeTitle.startAnimation(fadeIn);
                mode.startAnimation(fadeIn);
                buttonRight.startAnimation(fadeIn);
                buttonLeft.startAnimation(fadeIn);
                tictactoexo.startAnimation(fadeIn);
                leave.startAnimation(fadeIn);
            }
        }
        else{
            ImageButton gobackbtn = (ImageButton) findViewById(R.id.goback);
            ImageButton restartbtn = (ImageButton) findViewById(R.id.restart);
            //ImageView tictactoecell = (ImageView) findViewById(R.id.tictactoecell);
            TextView winner = (TextView) findViewById(R.id.winner);

            if(start) {
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setEnabled(false);
                    buttons[i].startAnimation(fadeOut);
                }
                gobackbtn.startAnimation(fadeOut);
                restartbtn.startAnimation(fadeOut);
                winner.startAnimation(fadeOut);

                gobackbtn.setEnabled(false);
                restartbtn.setEnabled(false);
                winner.setEnabled(false);

                for(int i = 0; i < buttons.length; i++){
                    buttons[i].setContentDescription("-");
                }
                Arrays.fill(btnClone, 0);
                wnr=false;
                movenmb=0;
            }
            else{
                gobackbtn.startAnimation(fadeIn);
                restartbtn.startAnimation(fadeIn);
            }
        }
    }
    private void startGif(boolean goNext){
        int gif = R.drawable.tictactoegif;
        if(!goNext){gif=R.drawable.tictactoegifreverse;}
        ImageView tictactoesticks = (ImageView) findViewById(R.id.tictactoecell);
        ImageView gifImageView = (ImageView) findViewById(R.id.gifplace);
        //ImageView tictactoesticks = prov(goNext,1);
        //ImageView  gifImageView = prov(goNext,2);
        Glide.with(MainActivity.this)
                .asGif()
                .load(gif)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        tictactoesticks.setVisibility(View.INVISIBLE);
                        resource.setLoopCount(1);
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                if(goNext) {
                                    setContentView(R.layout.game_field);
                                    curentCV = R.layout.game_field;
                                    for (int i = 0; i < buttons.length; i++) {
                                        String btnId = "button" + (i + 1);
                                        int resID = getResources().getIdentifier(btnId, "id", getPackageName());
                                        buttons[i] = (ImageButton) findViewById(resID);
                                        buttons[i].setContentDescription("-");
                                        buttons[i].setOnClickListener(MainActivity.this);

                                        btnClone[i] = 0;

                                        ImageButton gobackbtn = (ImageButton) findViewById(R.id.goback);
                                        ImageView tictactoecell = (ImageView) findViewById(R.id.tictactoecell);
                                    }
                                }
                                else{
                                    setContentView(R.layout.start_screen);
                                    curentCV = R.layout.start_screen;
                                    TextView modeTxt = (TextView) findViewById(R.id.mode);
                                    if(modeNow){modeTxt.setText("With Friend");}else{modeTxt.setText("With Computer");}
                                }
                                startAnimation(!goNext, false);
                            }
                        });
                        return false;
                    }}).into(gifImageView);
    }
    private void setPar(int page, int par){
        if(page==1) {
            Button startbtn = (Button) findViewById(R.id.buttonStart);
            ImageView tictactoeTitle = (ImageView) findViewById(R.id.tictactoeTitle);
            TextView mode = (TextView) findViewById(R.id.mode);
            Button buttonRight = (Button) findViewById(R.id.buttonRight);
            Button buttonLeft = (Button) findViewById(R.id.buttonLeft);
            Button leave = (Button) findViewById(R.id.leave);
            ImageView tictactoexo = (ImageView) findViewById(R.id.tictactoexo);
            ImageView tictactoecell = (ImageView) findViewById(R.id.tictactoecell);
            if (par == 0) {
                startbtn.setVisibility(View.INVISIBLE);
                tictactoeTitle.setVisibility(View.INVISIBLE);
                mode.setVisibility(View.INVISIBLE);
                buttonRight.setVisibility(View.INVISIBLE);
                buttonLeft.setVisibility(View.INVISIBLE);
                tictactoexo.setVisibility(View.INVISIBLE);
                tictactoecell.setVisibility(View.INVISIBLE);
                leave.setVisibility(View.INVISIBLE);
            }
            if (par == 1) {
                startbtn.setVisibility(View.VISIBLE);
                tictactoeTitle.setVisibility(View.VISIBLE);
                mode.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.VISIBLE);
                buttonLeft.setVisibility(View.VISIBLE);
                tictactoexo.setVisibility(View.VISIBLE);
                tictactoecell.setVisibility(View.VISIBLE);
                leave.setVisibility(View.VISIBLE);
            }
        }
        else if(page==2){
            ImageButton gobackbtn = (ImageButton) findViewById(R.id.goback);
            ImageView tictactoecell = (ImageView) findViewById(R.id.tictactoecell);
            TextView winner = (TextView) findViewById(R.id.winner);

            if (par == 0) {
                gobackbtn.setVisibility(View.INVISIBLE);
                tictactoecell.setVisibility(View.INVISIBLE);
                gobackbtn.setEnabled(false);
                for(int i = 0; i < buttons.length; i++){
                    buttons[i].setImageResource(0);
                    buttons[i].setEnabled(false);
                }
            }
            if (par == 1) {
                gobackbtn.setVisibility(View.VISIBLE);
                tictactoecell.setVisibility(View.VISIBLE);
                gobackbtn.setEnabled(true);
                for(int i = 0; i < buttons.length; i++){
                    buttons[i].setEnabled(true);
                }
            }
            if (par == 2){
                for(int i = 0; i < buttons.length; i++){
                    buttons[i].setImageResource(0);
                    buttons[i].setContentDescription("-");
                }
                Arrays.fill(btnClone, 0);
                wnr=false;
                movenmb=0;
                winner.setText("");
            }
        }
    }
}
