package com.example.recycup_cafe;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;         // 핵심 모듈

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // xml 파일에 정의된 TextView 객체 얻기
        final TextView tv_output = findViewById(R.id.tv_output);

        // R.id.button_1 : 첫 번째 버튼을 가리키는 id
        // setOnClickListener : 버튼이 눌렸을 때 호출될 함수 설정
        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            // 리스너의 기능 중에서 클릭(single touch) 사용
            @Override
            public void onClick(View v) {
                // input : 텐서플로 모델의 placeholder에 전달할 데이터(3)
                // output: 텐서플로 모델로부터 결과를 넘겨받을 배열. 덮어쓰기 때문에 초기값은 의미없다.
                int[] input = new int[]{3};
                int[] output = new int[]{0};    // 15 = 3 * 5, out = x * 5

                // 1번 모델을 해석할 인터프리터 생성
                Interpreter tflite = getTfliteInterpreter("simple_1.tflite");

                // 모델 구동.
                // 정확하게는 from_session 함수의 output_tensors 매개변수에 전달된 연산 호출
                tflite.run(input, output);

                // 출력을 배열에 저장하기 때문에 0번째 요소를 가져와서 문자열로 변환
                tv_output.setText(String.valueOf(output[0]));
            }
        });

        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 데이터 2개 사용. [][]는 2차원 배열을 의미한다.
                int[][] input = new int[][]{{3}, {7}};
                int[] output = new int[]{0};        // 58 = (3 * 3) + (7 * 7), out = sum(x * x)

                Interpreter tflite = getTfliteInterpreter("simple_2.tflite");
                tflite.run(input, output);

                tv_output.setText(String.valueOf(output[0]));

                // 아래 코드는 에러.
                // 텐서플로의 벡터 연산을 자바쪽에서 풀어서 계산해야 하는데,
                // 구성 요소가 객체 형태로 존재하지 않을 경우 shape이 일치하지 않아서 발생하는 에러
                // int[] input = new int[]{3, 7};
                //
                // 모델을 구성할 때 사용한 코드. x * x는 배열간의 연산이다.
                // x = tf.placeholder(tf.int32, shape=[2])
                // out = tf.reduce_sum(x * x)
            }
        });

        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력 변수를 별도로 2개 구성
                int[] input_1 = new int[]{3};
                int[] input_2 = new int[]{7};
                int[][] inputs = new int[][]{input_1, input_2};

                // 출력은 하나지만, 함수 매개변수를 맞추기 위해 맵 생성
                java.util.Map<Integer, Object> outputs = new java.util.HashMap();

                // 출력을 받아올 변수 1개 추가
                int[] output_1 = new int[]{0};      // 10 = 3 + 7, out = x + y
                outputs.put(0, output_1);

                Interpreter tflite = getTfliteInterpreter("simple_3.tflite");

                // 구동 함수는 run과 지금 이 함수밖에 없다.
                // runForMultipleInputsOutputs 함수는 입력도 여럿, 출력도 여럿이다.
                // 입력은 입력들의 배열, 출력은 <Integer, Object> 형태의 Map.
                // key와 value에 해당하는 Integer와 Object 자료형은 변경할 수 없다.
                tflite.runForMultipleInputsOutputs(inputs, outputs);

                tv_output.setText(String.valueOf(output_1[0]));
            }
        });

        findViewById(R.id.button_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 변수 2개를 전달하는 방법으로 앞에서처럼 해도 되지만 이번 코드가 간결하다.
                int[][] inputs = new int[][]{{3}, {7}};

                java.util.Map<Integer, Object> outputs = new java.util.HashMap();

                // 별도 변수없이 직접 put 함수에 전달하면서 배열 생성
                outputs.put(0, new int[]{0});       // 10, 21 = 3 + 7, 3 * 7 : out1, out2 = x + x, y * y
                outputs.put(1, new int[]{0});

                Interpreter tflite = getTfliteInterpreter("simple_4.tflite");
                tflite.runForMultipleInputsOutputs(inputs, outputs);

                // 별도로 출력 변수를 정의하지 않았기 때문에 Map 클래스의 get 함수를 통해 가져온다.
                // Object 자료형을 배열로 변환해서 사용
                int[] output_1 = (int[]) outputs.get(0);
                int[] output_2 = (int[]) outputs.get(1);
                tv_output.setText(String.valueOf(output_1[0]) + " : " + String.valueOf(output_2[0]));
            }
        });
    }

    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(MainActivity.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}

