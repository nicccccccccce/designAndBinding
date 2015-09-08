package demo.test.chi.test;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Objects;

import demo.test.chi.mode.User;
import demo.test.chi.mode.UserBean;
import demo.test.chi.test.databinding.AcUserBinding;

/**
 * Created by eve on 2015/9/7.
 */
public class BindingActivity extends AppCompatActivity {
    UserBean bean;
    AcUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.ac_user);
        bean = new UserBean("小巧", 22);
        binding.setUser(bean);
        binding.setButtonname("点击");
//        ObservableArrayMap<String,Object>  data1=new ObservableArrayMap<>();
//        data1.put("firstName","long to no see");
//        binding.setUser(data1);
//        ObservableList<UserBean>   data=new ObservableArrayList<UserBean>();
//        data.add( new UserBean("小巧", 22));
//        binding.setUserlist(data);
        binding.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setAge(18);
                bean.setUserName("小花");
                binding.setUser(bean);
                binding.setButtonname("第一项");
            }
        });

    }
}
