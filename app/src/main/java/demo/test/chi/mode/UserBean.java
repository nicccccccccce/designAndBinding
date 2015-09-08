package demo.test.chi.mode;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by eve on 2015/9/7.
 */
public class UserBean {
    public ObservableField<String> userName = new ObservableField<String>();
    public ObservableInt age = new ObservableInt();

    public UserBean() {
    }

    public UserBean(String userName, int age) {
        this.userName.set(userName);
        this.age.set(age);
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }
}
