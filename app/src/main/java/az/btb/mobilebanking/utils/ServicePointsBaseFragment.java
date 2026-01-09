package az.btb.mobilebanking.utils;

import androidx.databinding.ViewDataBinding;

import az.btb.mobilebanking.models.ServicePoint;

public abstract class ServicePointsBaseFragment<Binding extends ViewDataBinding> extends Fragment<Binding> {

    public ServicePointsBaseFragment(int layoutRes) {
        super(layoutRes);
    }

    public abstract void selectServicePoint(ServicePoint point);
    public abstract void resetView();
}
