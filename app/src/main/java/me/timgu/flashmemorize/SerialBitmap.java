//Disclaimer: I am not the original author of this code. The code is found at
//https://stackoverflow.com/questions/6002800/android-serializable-problem
package me.timgu.flashmemorize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class SerialBitmap implements Serializable {
    //This class is intended to make bitmaps serializable. Can also convert bitmaps to
    //このクラスはビットマップをシリアル化（シリアライズとは入出力処理）可能にすることを目的としています。 ビットマップに変換することもできます
    //encoded string (for JSON conversion).

    public Bitmap bitmap;

//＠notnullとはメソッドについてればnullを返さない。変数の場合nullを持てないという注釈。逆に＠nullableはnullが許容される
    public SerialBitmap(@NotNull File imgFile) {
        // Take your existing call to BitmapFactory and put it here
        //BitmapFactoryへの既存の呼び出しを受け取り、ここに配置します
        bitmap = BitmapFactory.decodeFile(imgFile.getPath());//ファイルのパスを渡す（画像ファイルをビットマップに変換）
        int pause = 1;
    }

    public SerialBitmap(@NotNull Bitmap image){
        bitmap = image;
    }//nullがパラメータに渡されると失敗するそしてここでもビットマップを作る

    public SerialBitmap(@NotNull String encodedString){
        //This constructor get bitmap from string//符号化された文字列（画像データ）をデコードしてビットマップに変換
        if (encodedString.length()==0){
            bitmap = null;
        }else {

            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);//文字をデコード（前の因数がエンコードされた文字列、後ろは決まり文句）
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);//ビットマップ作成
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    // Converts the Bitmap into a byte array for serialization
    //オブジェクトに対する出力処理（書き込み）のためにビットマップをバイト配列（元の画像データ）に変換します　　　　　　　　　　　　　　　　　　　
    private void writeObject(@NotNull java.io.ObjectOutputStream out) throws IOException {//オブジェクトの書き込みを行うクラスのオブジェクトoutを受け取る
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();//バイト出力を行うクラスのオブジェクトを生成。つまりバイナリデータの書き込み
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);//ビットマップからPNGの形式に圧縮してbytestreamに渡す
            byte bitmapBytes[] = byteStream.toByteArray();//bytestreamからbyte配列を取り出す
            out.write(bitmapBytes, 0, bitmapBytes.length);//outにbyte配列を書き込み
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Deserializes a byte array representing the Bitmap and decodes it
    //ビットマップを表すバイト配列をオブジェクトから入力処理（読み込み）してデコード（ビットマップに変換）します
    private void readObject(@NotNull java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1)//引数として受け取ったobjectの中を読み取りbに保存
                byteStream.write(b);//指定されたbバイトをバイト配列出力ストリームに書き込み
            byte bitmapBytes[] = byteStream.toByteArray();//byte配列を取り出す
            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);//取り出したbyte配列からビットマップ作成
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String bitMapToString(Bitmap bitmap){//ビットマップを文字列に変換
        if (bitmap == null){
            return null;
        }
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();//バイナリデータの書き込みをするクラスのオブジェクト生成
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);//bitmapをPNG形式に圧縮しbaosに渡す
        byte [] b=baos.toByteArray();//byte配列を取り出す
        String temp= Base64.encodeToString(b, Base64.DEFAULT);//byte配列を文字列にエンコード
        return temp;
    }

    public String getAsString(){//上のメソッドと一緒
        if (bitmap == null){
            return "";
        }
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString){//文字列をビットマップに変換
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);//文字列をbyte配列に取り出す
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);//バイト配列をビットマップにデコード
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}