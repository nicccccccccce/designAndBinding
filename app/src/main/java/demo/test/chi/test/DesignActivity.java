package demo.test.chi.test;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;

import butterknife.ButterKnife;
import butterknife.InjectView;
import demo.test.chi.http.BaseHttp;

/**
 * Created by eve on 2015/9/1.
 */
public class DesignActivity extends BaseHttp {
    @InjectView(R.id.textInput)
    TextInputLayout input_lay;
    @InjectView(R.id.floating)
    FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design);
        ButterKnife.inject(this);

        input_lay = (TextInputLayout) findViewById(R.id.textInput);
        input_lay.setHint("请输入");
        input_lay.setHintAnimationEnabled(true);
        input_lay.setHintTextAppearance(R.style.TextStyle);
        input_lay.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        input_lay.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                try {
                    input_lay.setError("");
                    if (s.toString() != null)
                        Integer.parseInt(s.toString());
//                    input_lay.setErrorEnabled(false);
                } catch (Exception e) {
//                    input_lay.setErrorEnabled(true);
                    input_lay.setError("没救了");

                }

            }
        });
//        mFloatingActionButton.setBackgroundTintList();
//        mFloatingActionButton.setBackgroundTintMode(PorterDuff.Mode.DST_OVER);
    }
}
