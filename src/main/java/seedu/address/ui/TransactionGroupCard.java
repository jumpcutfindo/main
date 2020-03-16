package seedu.address.ui;

import java.util.Comparator;
import java.util.Objects;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.transaction.Date;
import seedu.address.model.transaction.Expense;
import seedu.address.model.transaction.Transaction;

/**
 * A UI component that displays information of a group of {@code Transaction}.
 */
public class TransactionGroupCard extends UiPart<Region> {

    private static final int LIST_CELL_HEIGHT = 32;
    private static final int LIST_OFFSET = 0;

    private static final String FXML = "TransactionGroupCard.fxml";

    private static final String POSITIVE_CLASS = "positive";
    private static final String NEGATIVE_CLASS = "negative";

    public final FilteredList<Transaction> transactionsList;

    private final ObservableList<Transaction> originalTransactionList;

    private int startIndex;

    @FXML
    private Label transactionGroupLabel;

    @FXML
    private Label groupExpenditureLabel;

    @FXML
    private ListView<Transaction> transactionItemsList;

    public TransactionGroupCard(ObservableList<Transaction> originalTransactionList,
                                FilteredList<Transaction> transactionsList, int startIndex) {
        super(FXML);
        this.originalTransactionList = originalTransactionList;
        this.transactionsList = transactionsList;
        this.startIndex = startIndex;

        Date groupDate = transactionsList.get(0).getDate();
        transactionGroupLabel.setText(String.format("%s %s %s", groupDate.getDate().getDayOfMonth(),
                groupDate.getMonth(), groupDate.getYear()));

        setGroupExpenditure();

        transactionItemsList.setItems(transactionsList);
        transactionItemsList.setCellFactory(listView -> new TransactionListViewCell());
        transactionItemsList.setPrefHeight(transactionsList.size() * LIST_CELL_HEIGHT + LIST_OFFSET);
    }

    private void setGroupExpenditure() {
        double groupValue = transactionsList.stream().mapToDouble(t -> {
            if (t instanceof Expense) {
                return -t.getAmount().amount;
            } else {
                return t.getAmount().amount;
            }
        }).sum();

        if (groupValue < 0) {
            groupExpenditureLabel.setText("-$" + String.format("%.2f", -groupValue));
            groupExpenditureLabel.getStyleClass().add(NEGATIVE_CLASS);
        } else {
            groupExpenditureLabel.setText("+$" + String.format("%.2f", groupValue));
            groupExpenditureLabel.getStyleClass().add(POSITIVE_CLASS);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Transaction} using a {@code TransactionCard}.
     */
    class TransactionListViewCell extends ListCell<Transaction> {
        @Override
        protected void updateItem(Transaction transaction, boolean empty) {
            super.updateItem(transaction, empty);

            if (empty || transaction == null) {
                setGraphic(null);
                setText(null);
            } else {
                int index = getIndex() + startIndex + 1;
                setGraphic(new TransactionCard(transaction, index).getRoot());
            }
        }
    }
}
