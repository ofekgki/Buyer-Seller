package Managers;

import java.util.Objects;

public class Action2 implements Observer{
    String className="Action2";

    @Override
    public void update(String msg) {

        System.out.println(className+ " got: "+ msg);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action2 action2 = (Action2) o;
        return Objects.equals(className, action2.className);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(className);
    }
}
