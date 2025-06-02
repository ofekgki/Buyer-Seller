package Commands;

import Managers.*;

public class Command102 implements Command {
    ManagerFacade facade;
    public Command102() {
        facade = ManagerFacade.getInstance();
    }

    @Override
    public void execute() {
      //  facade.printItemTwice();

    }
}
