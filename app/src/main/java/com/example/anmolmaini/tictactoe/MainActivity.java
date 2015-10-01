package com.example.anmolmaini.tictactoe;

import android.graphics.Color;
import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.util.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.InputStream;
import android.app.*;
import java.util.Random;

public class MainActivity extends Activity
{
    int []arena;
    int[][] bestmoves = new int [2][9];
    int lastmove, turns;
    private boolean Xturn, win_or_draw;
    Switch toggle;
    int [] arenaids = {(R.id.top_left_button), (R.id.top_center_button), (R.id.top_right_button),
            (R.id.center_left_button), (R.id.center_center_button), (R.id.center_right_button),
            (R.id.bottom_left_button), (R.id.bottom_center_button), (R.id.bottom_right_button)};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arena = new int[9];
        Xturn = true;
        win_or_draw = false;
        turns = 0;
        toggle = (Switch)findViewById(R.id.switch1);
        Toast.makeText(getApplicationContext(), "Player 1 Turn", Toast.LENGTH_SHORT).show();
    }
    public void  toggler(View v)
    {
        if(toggle.isChecked())
            toggle.setText("1P");
        else
            toggle.setText("2P");
        reset();
    }
    public void move(View v)
    {
            int pos = 0;
            int id = v.getId();
            switch (id) {
                case (R.id.top_left_button):
                    break;
                case (R.id.top_center_button):
                    pos = 1;
                    break;
                case (R.id.top_right_button):
                    pos = 2;
                    break;
                case  (R.id.center_left_button):
                    pos = 3;
                    break;
                case (R.id.center_center_button):
                    pos = 4;
                    break;
                case (R.id.center_right_button):
                    pos = 5;
                    break;
                case (R.id.bottom_left_button):
                    pos = 6;
                    break;
                case (R.id.bottom_center_button):
                    pos = 7;
                    break;
                case (R.id.bottom_right_button):
                    pos = 8;
                    break;
            }

            if (arena[pos] != 0) {
                Toast.makeText(getApplicationContext(), "Error: Not Empty. Try Again", Toast.LENGTH_LONG).show();
            }
        if(!toggle.isChecked())
        {
            Button selected = (Button) findViewById(id);
            if (Xturn)
            {
                arena[pos] = 1;
                Xturn = false;
                selected.setBackgroundColor(Color.parseColor("#CAA04B"));
                checkResult();
                if(!win_or_draw)
                    Toast.makeText(getApplicationContext(), "Player 2 Turn", Toast.LENGTH_SHORT).show();

            }
            else
            {
                arena[pos] = 2;
                Xturn = true;
                selected.setBackgroundColor(Color.parseColor("#188D54"));
                checkResult();
                if(!win_or_draw)
                    Toast.makeText(getApplicationContext(), "Player 1 Turn", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
                Button selected = (Button) findViewById(id);
                arena[pos] = 1;
                turns++;
                Xturn = false;
                selected.setBackgroundColor(Color.parseColor("#CAA04B"));
                checkResult();
                int comp_move = findBestMove();
                //Computer's Move
                if(turns < 8)
                {
                    arena[comp_move] = 2;
                    Button comp_button = (Button)findViewById(arenaids[comp_move]);
                    comp_button.setBackgroundColor(Color.parseColor("#188D54"));
                    turns++;
                    checkResult();
                }



        }

    }
    public int findBestMove()
    {
        int[] positions = getPos(arena);
        int bestmove = -1;
        int previous = Integer.MIN_VALUE;
        for(int i=0; i<positions.length; i++)
        {
            int[] child = arena.clone();
            child[positions[i]] = 2;
            lastmove = positions[i];
            int current = max(child);
            if(current > previous)
            {
                bestmove = positions[i];
                previous = current;
            }
        }
        return bestmove;
    }
    public int max(int[] Board)
    {
        if (minimaxWinner(Board)==2)
            return position(true);
        if (minimaxWinner(Board)==0)
            return position(false);
        if(minimaxWinner(Board)==1)
            return -3;
        int best = Integer.MAX_VALUE;
        int[] positions = getPos(Board);
        for(int i=0; i<positions.length; i++)
        {
            int[] child = Board.clone();
            child[positions[i]] = 'x';
            lastmove = positions[i];
            int current = min(child);
            if(current < best)
                best = current;
        }
        return best;
    }
    public int min(int[] Board){
        if (minimaxWinner(Board)== 2)
            return position(true);
        if (minimaxWinner(Board)==0)
            return position(false);
        if(minimaxWinner(Board)==1)
            return -3;
        int best = Integer.MIN_VALUE;
        int[] positions = getPos(Board);
        for(int i=0; i<positions.length; i++)
        {
            int[] child = Board.clone();
            child[positions[i]] = 'o';
            lastmove = positions[i];
            int current = max(child);
            if(current > best)
                best = current;
        }
        return best;
    }
    public static int[] getPos(int[] Board)
    {
        int arraylength = 0;
        for (int i=0; i<9; i++)
            if (Board[i] == 0)
                arraylength++;
        int[] array = new int[arraylength];
        int place = 0;
        for (int i=0; i<9; i++)
            if (Board[i] == 0)
            {
                array[place] = i;
                place++;
            }
        return array;
    }
    public static int minimaxWinner(int[] Board)
    {
        int winner = 3;
        boolean tie = true;
        for (int i=0; i<9; i++)
            if (Board[i] == 0)
                tie = false;
        for (int i=0; i<3; i++)
        {
            if ((Board[3*i]==Board[3*i+1]&&Board[3*i+1]==Board[3*i+2])&&(Board[3*i]==2||Board[3*i]==1))
                winner = Board[3*i];
            else if ((Board[i]==Board[i+3]&&Board[i+3]==Board[i+6])&&(Board[i]==2||Board[i]==1))
                winner = Board[i];
            else if ((Board[0]==Board[4]&&Board[4]==Board[8])&&(Board[i]==2||Board[i]==1))
                winner = Board[4];
            else if ((Board[2]==Board[4]&&Board[4]==Board[6])&&(Board[i]==2||Board[i]==1))
                winner = Board[4];
        }
        if (winner == 3 && tie)
            return 0;
        return winner;
    }
    public int position(boolean win)
    {
        if (win || (turns%2==1))
        {
            for (int i=0; i<9; i++)
                if (bestmoves[0][i] == lastmove)
                {
                    if (win)
                        return bestmoves[1][i];
                    return bestmoves[1][i] - 1;
                }
            if (win)
                return 1;
            return 0;
        }
        for (int i=0; i<9; i++)
            if (bestmoves[0][i]==lastmove)
                return -2;
        return 0;
    }
    public void checkResult()
    {
        boolean empty = false;
        for(int i = 0; i < 9; i++)
            if(arena[i] == 0)
            {
                empty = true;
                break;
            }
        //Check Horizontal Lines
        for(int i = 0; i < 7; i=i+3)
        {
            if(arena[i] == 1 && arena[i+1] == 1 && arena[i+2] == 1 && !win_or_draw)
            {
                Toast.makeText(getApplicationContext(), "Player 1 Wins", Toast.LENGTH_LONG).show();
                win_or_draw = true;
                over();
                image();
            }
            if(arena[i] == 2 && arena[i+1] == 2 && arena[i+2] == 2 && !win_or_draw)
            {
                if(!toggle.isChecked())
                    Toast.makeText(getApplicationContext(), "Player 2 Wins", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Computer Wins", Toast.LENGTH_LONG).show();
                win_or_draw = true;
                over();
                image();
            }
        }
        //Check Vertical Lines
        for(int i = 0; i < 3; i++)
        {
            if(arena[i] == 1 && arena[i+3] == 1 && arena[i+6] == 1 && !win_or_draw)
            {
                Toast.makeText(getApplicationContext(), "Player 1 Wins", Toast.LENGTH_LONG).show();
                win_or_draw = true;
                over();
                image();
            }
            if(arena[i] == 2 && arena[i+3] == 2 && arena[i+6] == 2 && !win_or_draw)
            {
                if(!toggle.isChecked())
                    Toast.makeText(getApplicationContext(), "Player 2 Wins", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Computer Wins", Toast.LENGTH_LONG).show();
                win_or_draw = true;
                over();
                image();
            }
        }
        //Check Diagnols
        if(arena[0] == 1 && arena[4] == 1 && arena[8] == 1 || arena[2] == 1 && arena[4] == 1 && arena[6] == 1 && !win_or_draw)
        {
            Toast.makeText(getApplicationContext(), "Player 1 Wins", Toast.LENGTH_LONG).show();
            win_or_draw = true;
            over();
            image();
        }
        if(arena[0] == 2 && arena[4] == 2 && arena[8] == 2 || arena[2] == 2 && arena[4] == 2 && arena[6] == 2 && !win_or_draw)
        {
            if(!toggle.isChecked())
                Toast.makeText(getApplicationContext(), "Player 2 Wins", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Computer Wins", Toast.LENGTH_LONG).show();
            win_or_draw = true;
            over();
            image();
        }
        if(!empty && !win_or_draw)
        {
            Toast.makeText(getApplicationContext(), "Draw", Toast.LENGTH_LONG).show();
            win_or_draw = true;
            over();
            image();
        }
    }

    public void over()
    {
        for(int id: arenaids)
        {
            Button button = (Button) findViewById(id);
            button.setClickable(false);
        }
    }

    public void reset(View view)
    {
        Xturn = true;
        win_or_draw = false;
        turns = 0;
        for(int i=0; i < 9; i++)
        {
            arena[i] = 0;
        }
        for(int id: arenaids)
        {
            Button button = (Button)findViewById(id);
            button.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button.setClickable(true);
        }
        Toast.makeText(getApplicationContext(), "Player 1 Turn", Toast.LENGTH_SHORT).show();
    }
    public void reset()
    {
        Xturn = true;
        win_or_draw = false;
        turns = 0;
        for(int i=0; i < 9; i++)
        {
            arena[i] = 0;
        }
        for(int id: arenaids)
        {
            Button button = (Button)findViewById(id);
            button.setBackgroundColor(Color.parseColor("#FFFFFF"));
            button.setClickable(true);
        }
        Toast.makeText(getApplicationContext(), "Player 1 Turn", Toast.LENGTH_SHORT).show();
    }

    public void image()
    {
        Log.i("Hello", "Image");
        ImageView image = new ImageView(this);
        String url = getRandomPicture();
        new DownloadImageTask(image).execute(url);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).setMessage("Thanks for playing. Here's a random imgur " +
                        "picture for you").setView(image);
        builder.create().show();


        //im.onPostExecute();
    }

    public String getRandomPicture()
    {
        String url = "http://i.imgur.com/";
        char[] possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        Random r = new Random();
        for(int i = 0; i < 5; i++)
        {
            char c = possible[r.nextInt(possible.length)];
            sb.append(c);
        }
        sb.append(".png");
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
