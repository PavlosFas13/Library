package com.mgiandia.library.view.Borrower.ManageBorrowers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import com.mgiandia.library.R;
import com.mgiandia.library.memorydao.BorrowerDAOMemory;
import com.mgiandia.library.util.Quadruple;
import com.mgiandia.library.view.Borrower.AddEditBorrower.AddEditBorrowerActivity;
import com.mgiandia.library.view.Borrower.BorrowerDetails.BorrowerDetailsActivity;
import com.mgiandia.library.view.Loans.ManageLoans.ManageLoansActivity;
import com.mgiandia.library.view.Returns.ManageReturns.ManageReturnsActivity;
import com.mgiandia.library.view.Util.AdvancedListAdapter;

/**
 * @author Νίκος Σαραντινός
 *
 * Υλοποιήθηκε στα πλαίσια του μαθήματος Τεχνολογία Λογισμικού το έτος 2016-2017 υπό την επίβλεψη του Δρ. Βασίλη Ζαφείρη.
 *
 */

public class ManageBorrowersActivity extends AppCompatActivity implements ManageBorrowersView, SearchView.OnQueryTextListener {
    ManageBorrowersPresenter presenter;

    private ListView itemListView;
    private SearchView searchListView;
    private AdvancedListAdapter adapter;

    /**
     * Δημιουργεί το layout και αρχικοποιεί
     * το activity.
     * @param savedInstanceState το Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_items);

        adapter = new AdvancedListAdapter(this);

        itemListView = (ListView) findViewById(R.id.item_list_view);
        itemListView.setAdapter(adapter);
        itemListView.setTextFilterEnabled(true);

        searchListView = (SearchView) findViewById(R.id.items_list_search_view);
        searchListView.setIconifiedByDefault(false);
        searchListView.setOnQueryTextListener(this);

        presenter = new ManageBorrowersPresenter(this, new BorrowerDAOMemory());

        findViewById(R.id.item_add_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onStartAddNew();
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onClickItem(((Quadruple) parent.getItemAtPosition(position)).getUID());
            }
        });
    }

    /**
     * Τροποποιεί το κείμενο του δανειζόμενου.
     * @param text Το κείμενο που θα τροποποιηθεί
     * @return true
     */
    public boolean onQueryTextChange(String text) {
        if (TextUtils.isEmpty(text))
            itemListView.clearTextFilter();
        else
            itemListView.setFilterText(text);

        return true;
    }

    /**
     * Υποβάλλει το κείμενο του συγγραφέα.
     * @param query Θέτει το κείμενο ως query
     * @return false
     */
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Αδειάζει το κείμενο που βρίσκεται
     * μέσα στην μπάρα αναζήτησης.
     */
    private void clear_search_bar() {
        searchListView.setQuery("", false);
        searchListView.clearFocus();
        presenter.onLoadSource();
    }

    /**
     * Αδειάζει την μπάρα αναζήτησης
     * @param requestCode Ο ζητούμενος κωδικός
     * @param resultCode Ο κωδικός του αποτελέσματος
     * @param data Το intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            clear_search_bar();
            presenter.onShowToast(data.getStringExtra("message_to_toast"));
        } else if (requestCode == 1) {
            clear_search_bar();

            if (resultCode == Activity.RESULT_OK)
                presenter.onShowToast(data.getStringExtra("message_to_toast"));
        }
    }

    /**
     * Φορτώνει την λίστα με τους δανειζομένους.
     * @param input Η λίστα που θα φορτώσει
     */
    public void loadSource(List<Quadruple> input) {
        adapter.loadSource(input);
    }

    /**
     * Μεταφέρει τον χρήστη στο activity ManageLoansActivity
     * όταν γίνει click πάνω στον δανειζόμενο με id uid.
     * @param uid Το μοναδικό id του δανειζόμενου
     */
    public void clickItemLoans(int uid)
    {
        Intent intent = new Intent(this, ManageLoansActivity.class);
        intent.putExtra("borrower_id",uid);
        startActivityForResult(intent, 1);
    }

    /**
     * Μεταφέρει τον χρήστη στο activity ManageReturnsActivity
     * όταν γίνει click πάνω στον δανειζόμενο με id uid.
     * @param uid Το μοναδικό id του δανειζόμενου
     */
    public void clickItemReturns(int uid)
    {
        Intent intent = new Intent(this, ManageReturnsActivity.class);
        intent.putExtra("borrower_id",uid);
        startActivityForResult(intent, 1);
    }

    /**
     * Μεταφέρει τον χρήστη στο activity BorrowerDetailsActivity
     * όταν γίνει click πάνω στον δανειζόμενο με id uid.
     * @param uid Το μοναδικό id του δανειζόμενου
     */
    public void clickItem(int uid)
    {
        Intent intent = new Intent(this, BorrowerDetailsActivity.class);
        intent.putExtra("borrower_id", uid);
        startActivityForResult(intent, 1);
    }

    /**
     * Ξεκινάει το activity AddEditBorrowerActivity
     */
    public void startAddNew()
    {
        Intent intent = new Intent(this, AddEditBorrowerActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * Αν πρέπει να επιστρέψει τα δάνεια
     * @return Τα επιστρεφόμενα δάνεια
     */
    public boolean shouldLoadLoansOnClick()
    {
        return this.getIntent().hasExtra("should_load_loans");
    }

    /**
     * Αν πρέπει να επιστρέψει τις επιστροφές
     * @return Οι επιστροφές
     */
    public boolean shouldLoadReturnsOnClick()
    {
        return this.getIntent().hasExtra("should_load_returns");
    }

    /**
     * Εμφανίζει ένα Toast.
     * @param value Το περιεχόμενο που θα εμφανιστεί
     */
    public void showToast(String value)
    {
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
    }
}
