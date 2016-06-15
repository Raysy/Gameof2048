package raysi.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raysi on 2016-6-14.
 */
public class GameView extends GridLayout {

    private float startX, startY, offsetX, offsetY;
    private Card card;
    private Card[][] cardMaps;
    private List<Point> emptyPoints;
    private boolean merge;
    private boolean complete;

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GameView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);
        cardMaps = new Card[4][4];
        emptyPoints = new ArrayList<Point>();
        merge = false;
        complete = true;

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("raysi", "onTouch");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {//是否左右滑动
                            if (offsetX < -5) {
                                System.out.println("left");
                                swipeLeft();
                            } else if (offsetX > 5) {
                                System.out.println("right");
                                swipeRight();
                            }
                        } else {//是否上下滑动
                            if (offsetY < -5) {
                                System.out.println("up");
                                swipeUp();
                            } else if (offsetY > 5) {
                                System.out.println("down");
                                swipeDown();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w, h) - 10) / 4;
        addCards(cardWidth, cardWidth);
        startGame();
    }

    private void swipeDown() {
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardMaps[x][y1].getNum() > 0) {
                        if (cardMaps[x][y].getNum() <= 0) {
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);
                            y++;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x][y1])) {
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum() * 2);
                            cardMaps[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        isAdd();
    }

    private void swipeUp() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardMaps[x][y1].getNum() > 0) {
                        if (cardMaps[x][y].getNum() <= 0) {
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);
                            y--;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x][y1])) {
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum() * 2);
                            cardMaps[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        isAdd();
    }

    private void swipeRight() {
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardMaps[x1][y].getNum() > 0) {
                        if (cardMaps[x][y].getNum() <= 0) {
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);
                            x++;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x1][y])) {
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum() * 2);
                            cardMaps[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        isAdd();
    }

    private void swipeLeft() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardMaps[x1][y].getNum() > 0) {
                        if (cardMaps[x][y].getNum() <= 0) {
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);
                            x--;
                            merge = true;
                        } else if (cardMaps[x][y].equals(cardMaps[x1][y])) {
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum() * 2);
                            cardMaps[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
       isAdd();
    }

    private void isAdd(){
        if (merge){
            addRandomNumber();
        }
        checkComplete();
    }

    private void addCards(int cardWidth, int cardHeight) {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                card = new Card(getContext());
                card.setNum(0);
                addView(card, cardWidth, cardHeight);
                cardMaps[x][y] = card;
            }
        }

    }

    private void addRandomNumber() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardMaps[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        Point point = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        cardMaps[point.x][point.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    private void startGame() {
        MainActivity.getMainActivity().clearScore();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardMaps[x][y].setNum(0);
            }
        }
        addRandomNumber();
        addRandomNumber();
    }

    private void checkComplete(){
        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++)
                if (cardMaps[x][y].getNum() == 0 ||
                        (x > 0 && cardMaps[x][y].equals(cardMaps[x - 1][y])) ||
                        (x < 3 && cardMaps[x][y].equals(cardMaps[x + 1][y])) ||
                        (y > 0 && cardMaps[x][y].equals(cardMaps[x][y - 1])) ||
                        (y < 3 && cardMaps[x][y].equals(cardMaps[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("Tips").setMessage("Game Over!").setPositiveButton("Play Again", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }

    }
}
