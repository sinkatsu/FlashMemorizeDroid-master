package me.timgu.flashmemorize;

import android.content.Intent;
import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Card implements Serializable {

    public int side = 1;
    public int timesStudied = 0;
    public int timesCorrect = 0;
    public int viewed = 0;

    private String front;
    private String back;
    private int id ;
    private List<Integer> studyTrend = new ArrayList<>();
    private boolean front_pic_exist = false;
    private boolean back_pic_exist = false;

    private SerialBitmap front_pic;
    private SerialBitmap back_pic;

    public Card (String front_arg, String back_arg, int ID_arg, File front_pic_file, File back_pic_file){//手動でカードを作成
        front = front_arg;//カードの表面のテキスト
        back = back_arg;//カード裏面のテキスト
        id = ID_arg;

        if (front_pic_file != null && front_pic_file.exists()){//画像ファイルが存在している場合
            this.front_pic = new SerialBitmap(front_pic_file);//bitmap作成
        }else{
            this.front_pic = null;
        }

        if (back_pic_file != null && back_pic_file.exists()){
            this.back_pic = new SerialBitmap(back_pic_file);
        }else{
            this.back_pic = null;
        }
    }


    //このクラスに新しい変数が追加されるたびに更新する必要があります//JavaでJSONデータを使うことで、データベースをテキスト形式で簡単にかつ軽量に扱うことができるので便利です（データ交換フォーマット）
    //このコンストラクタはオブジェクトからカードを作る
    public Card(JSONObject obj){//引数にJSON形式のテキストを渡す。中のキーからデータを取り出す
        try{
            side = Integer.valueOf( obj.get("side").toString());//JSON形式のデータをint型の整数値に変換
            timesStudied = Integer.valueOf(obj.get("timesStudied").toString());//JSON形式のデータを整数値に変換
            timesCorrect = Integer.valueOf(obj.get("timesCorrect").toString());
            viewed = Integer.valueOf(obj.get("viewed").toString());
            front = obj.get("front").toString();//文字列を代入
            back = obj.get("back").toString();
            id = Integer.valueOf(obj.get("id").toString());
            front_pic =  new SerialBitmap(obj.get("front_pic").toString());//文字列からbitmapを作成
            back_pic = new SerialBitmap(obj.get("back_pic").toString());
        } catch (JSONException e){
            e.printStackTrace();
        }
    }


    //TODO: WIP
    public JSONObject onSave(){//JSON形式のデータを扱うオブジェクトを作るメソッド　名前をキーにしてデータを入れる//オブジェクトを更新するメソッド
        JSONObject obj = new JSONObject();

        try {
            obj.put("side", side);
            obj.put("timesStudied", timesStudied);//勉強回数？
            obj.put("timesCorrect", timesCorrect);//問題を解いた回数？
            obj.put("viewed", viewed);//見た回数？
            obj.put("front", front);//表面のテキスト
            obj.put("back", back);//裏面のテキスト
            obj.put("id", id);
            if (front_pic != null){
                obj.put("front_pic",front_pic.getAsString());}//bitmapの文字データを入れる
                else{obj.put("front_pic","");}
            if (back_pic != null){
                obj.put("back_pic",back_pic.getAsString());}
                else{obj.put("back_pic","");}
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //This method is currently obsolete as it is been replaced by the Card constructor
    //for JSON files. It has been kept as a reference.//このメソッドは、JSONファイルのCardコンストラクターに置き換えられたため、現在は使用されていません。 参考までに保管しております。
    public void onRead(JSONObject obj){
        try{
            side = Integer.valueOf( obj.get("side").toString());
            timesStudied = Integer.valueOf(obj.get("timesStudied").toString());
            timesCorrect = Integer.valueOf(obj.get("timesCorrect").toString());
            viewed = Integer.valueOf(obj.get("viewed").toString());
            front = obj.get("front").toString();
            back = obj.get("back").toString();
            id = Integer.valueOf(obj.get("id").toString());
            front_pic =  new SerialBitmap(obj.get("front_pic").toString());
            back_pic = new SerialBitmap(obj.get("back_pic").toString());
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void flip(){
        side = abs(side -1);
    }//－１して絶対値（０からの距離）を受け取るメソッド

    public String show(){//テキストの裏表をsideによって表示させる
        if (side == 1){
            return front;
        }else{
            return back;
        }
    }

    public Bitmap showImage(){//sideによって表裏の画像を表示
        if (side == 1 && front_pic != null){
            return front_pic.bitmap;
        }else if(side == 0 && back_pic != null){
            return back_pic.bitmap;
        }
        return null;
    }

    public double getStats(){//時間に関する統計を渡す（よくわからない部分）
        if (timesStudied != 0){
            double stats;
            stats = timesCorrect*1.0/timesStudied; //1.0 here implicit cast to double　1.0ここで暗黙的にdoubleにキャスト　勉強回数で解いた回数を割っている
            return stats;
        }else{
            return 1.0;
        }
    }

    public void editText(String text){//文字列をfront or backに代入//テキストを編集する時に使う変数と思われる
        if (side == 1){
            front = text;
        }else{
            back = text;
        }
    }

    public int getId(){
        return id;
    }

    public void setId(int ID){
        id = ID;
    }

    public void updateStudyTrend(int correct){
        studyTrend.add(correct);
    }//勉強の傾向を計るメソッド（回数を数える？）


    public void addPic(Bitmap image){//カードの裏表に画像を追加する
        if (side == 1){
            front_pic = new SerialBitmap(image);//ビットマップ作成
            front_pic_exist = true;//画像が存在することを証明
        }else{
            back_pic = new SerialBitmap(image);
            back_pic_exist = true;
        }
    }

    public void deletePic(){//画像を削除
        if (side == 1){
            front_pic = null;
            front_pic_exist = false;
        }else{
            back_pic = null;
            back_pic_exist = false;
        }
    }
    /*
    //The following are place holders for future implementations.

    public double[] getStudyTrend(){}
    public void addPic(){}
    public void showPic() {}
     */

}
