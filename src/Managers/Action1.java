package Managers;

import java.util.Objects;

public class Action1 implements Observer{

    String className="Action1";


    @Override
    public void update(String msg) {
        System.out.println(className + " got: "+ msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action1 action1 = (Action1) o;
        return Objects.equals(className, action1.className);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(className);
    }
}
