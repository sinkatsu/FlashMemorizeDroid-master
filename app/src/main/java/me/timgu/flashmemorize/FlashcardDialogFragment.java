package me.timgu.flashmemorize;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class FlashcardDialogFragment extends DialogFragment {

    public interface FlashcardDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String msg);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    FlashcardDialogListener listener;

    @Override
    public void onAttach(Context context) {//アプリの状態、環境情報をandroid os単位で受け渡しするためのインターフェース、例えば別のアプリからの応答に答えたり、別のアプリにアクセスするときに使う
        super.onAttach(context);
        // ホストアクティビティがコールバックインターフェイスを実装していることを確認する
        try {

            //ホストにイベントを送信できるように、NoticeDialogListenerをインスタンス化します
            listener = (FlashcardDialogListener) context;
        } catch (ClassCastException e) {
            // アクティビティはインターフェースを実装していません、例外をスローします
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Builderクラスを使用して便利なダイアログ構築
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View dialogView = inflater.inflate(R.layout.flashcard_dialog,null);

        builder.setView(dialogView)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText number = dialogView.findViewById(R.id.flashcard_dialog_number);
                        String msg = number.getText().toString();
                        listener.onDialogPositiveClick(FlashcardDialogFragment.this,msg);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(FlashcardDialogFragment.this);
                    }
                });
        // AlertDialogオブジェクトを作成して返す
        return builder.create();
    }
}
