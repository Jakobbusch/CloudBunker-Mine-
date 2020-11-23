package user.viewModel;


import user.model.ModelFactory;
import user.viewModel.adminViewModel.AdminViewModel;
import user.viewModel.editProfileViewModel.EditProfileViewModel;
import user.viewModel.homescreenViewModel.HomescreenViewModel;
import user.viewModel.loginViewModel.LoginViewModel;

public class ViewModelFactory {
    private LoginViewModel loginViewModel;
    private HomescreenViewModel homescreenViewModel;
    private AdminViewModel adminViewModel;
    private EditProfileViewModel editProfileViewModel;


    /**
     * A construtor for initializing the ViewModelFactory
     * @param mf a ModelFactory that is used to initialize the LoginViewModel
     */
    public ViewModelFactory(ModelFactory mf)
    {
        loginViewModel=new LoginViewModel(mf.getModel());
        homescreenViewModel = new HomescreenViewModel((mf.getModel()));
        adminViewModel = new AdminViewModel(mf.getModel());
        editProfileViewModel = new EditProfileViewModel(mf.getModel());
    }

    /**
     * A method for returning the LoginViewModel
     * @return a LoginViewModel object
     */
    public LoginViewModel getLoginViewModel()
    {
        return  loginViewModel;
    }

    /**
     * A method for returning the HomeScreenVieModel
     * @return A HomeScreenViewModel  object
     */
    public HomescreenViewModel getHomescreenViewModel(){return homescreenViewModel;}

    /**
     * A method for returning the AdminViewModel
     * @return An AdminViewModel Object
     */
    public AdminViewModel getAdminViewModel(){return adminViewModel;}

    /**
     * A method for returning the EditProfileViewModel
     * @return A EditProfileViewModel Object
     */
    public EditProfileViewModel getEditProfileViewModel() { return editProfileViewModel;
    }
}
