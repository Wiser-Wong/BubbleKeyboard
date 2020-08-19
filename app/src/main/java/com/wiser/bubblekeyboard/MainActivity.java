package com.wiser.bubblekeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wiser.bubblekeyboard.bubble.BubbleData;
import com.wiser.bubblekeyboard.bubble.BubbleSelectView;
import com.wiser.bubblekeyboard.keyboard.IssueTextView;
import com.wiser.bubblekeyboard.record.OnRecordListener;
import com.wiser.bubblekeyboard.record.RecordView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BubbleSelectView bsvList = findViewById(R.id.bsv_list);
		bsvList.setItems(getData());

//		final TextView tvTime = findViewById(R.id.tv_time);
//		RecordView recordView = findViewById(R.id.record_view);
//		// recordView.setProgress(50);
//		recordView.setOnRecordListener(new OnRecordListener() {
//
//			@Override public void onStartRecord() {
//
//			}
//
//			@Override public void onProgress(int time) {
//				System.out.println("----------当前时间-->>" + time);
//				tvTime.setText(time + "s");
//			}
//
//			@Override public void onFinishRecord() {
//				Toast.makeText(MainActivity.this, "录制完成", Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		IssueTextView tvIssue = findViewById(R.id.tv_issue);
//		tvIssue.updateIssueButtonUi(true);

		findViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,KeyboardActivity.class));
			}
		});

	}

	private List<BubbleData> getData() {
		List<BubbleData> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			BubbleData bubbleData = new BubbleData();
			if (i == 0) bubbleData.type = BubbleData.NO_BUBBLE_TYPE;
			else bubbleData.type = BubbleData.HAVE_BUBBLE_TYPE;
			list.add(bubbleData);
		}
		return list;
	}
}