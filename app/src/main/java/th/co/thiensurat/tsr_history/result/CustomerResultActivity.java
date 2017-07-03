package th.co.thiensurat.tsr_history.result;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter>
    implements CustomerResultInterface.view{

    @Override
    public CustomerResultInterface.presenter createPresenter() {
        return CustomerResultPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_result;
    }

    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {

    }

    @Override
    public void setupView() {

    }

    @Override
    public void initialize() {

    }
}
