package Commands;

import Managers.*;

public class Command101 implements Command {
    ManagerFacade facade;
    public Command101() {
        facade = ManagerFacade.getInstance();
    }

    @Override
    public void execute() {
     //   facade.printCountOfItemInMap();

    }
}
