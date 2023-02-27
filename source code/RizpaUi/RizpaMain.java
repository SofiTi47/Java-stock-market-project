public class RizpaMain {
    public static void main(String[] args) {
        Ui rizpaUi = new RizpaUi();
        rizpaUi.printMenu();
        RizpaUi.MenuOptions option = rizpaUi.getCommand(RizpaUi.MenuOptions.class);
        while(option != RizpaUi.MenuOptions.EXIT) {
            switch (option) {
                case LOADFILE:
                    rizpaUi.loadFile();
                    break;
                case GETSTOCKS:
                    rizpaUi.getStocks();
                    break;
                case GETSTOCK:
                    rizpaUi.getStock();
                    break;
                case STOCKACTION:
                   rizpaUi.tradeStocks();
                    break;
                case PENDINGACTIONS:
                    rizpaUi.getPendingTransactions();
                    break;
                case SAVEFILE:
                    rizpaUi.saveSystemData();
                    break;
                case LOADSAVED:
                    rizpaUi.loadSavedData();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
            rizpaUi.printMenu();
            option = rizpaUi.getCommand(RizpaUi.MenuOptions.class);
        }
        try {
            rizpaUi.exitSystem();
        } catch (InterruptedException e) {
            return;
        }
    }
}
