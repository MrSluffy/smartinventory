package com.smart.inventory.application.data.services.owner;

import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entity.Owner;
import com.smart.inventory.application.data.repository.IOwnerRepository;
import com.smart.inventory.application.views.MainLayout;
import com.smart.inventory.application.views.list.dashboard.DashboardView;
import com.smart.inventory.application.views.list.ingredient.IngredientView;
import com.smart.inventory.application.views.list.item.ItemView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerService implements IOwnerService {

    public record AuthorizedRoute(String route,
                                  String name,
                                  Class<? extends Component> view) {

    }

    public class AuthException extends Exception {

    }

    private final IOwnerRepository ownerRepository;

    @Autowired
    public OwnerService(IOwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void authenticate(String email, String password) throws AuthException {
        Owner owner = ownerRepository.getByEmail(email);
        if (owner != null && owner.verifyPassword(password)) {
            VaadinSession.getCurrent().setAttribute(Owner.class, owner);
            createRoutes(owner.getRoles());
        } else {
            throw new AuthException();
        }

    }

    private void createRoutes(Role roles) {
        getAuthorizedRoutes(roles).stream()
                .forEach(authorizedRoute ->
                        RouteConfiguration.forSessionScope()
                                .setRoute(authorizedRoute.route,
                                        authorizedRoute.view(),
                                        MainLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {

        var routes = new ArrayList<AuthorizedRoute>();

        if (role.equals(Role.OWNER)) {
            routes.add(new AuthorizedRoute("item", "Item Stock", ItemView.class));
            routes.add(new AuthorizedRoute("costing", "Costing", IngredientView.class));
        } else if (role.equals(Role.EMPLOYER)) {
            routes.add(new AuthorizedRoute("item", "Item Stock", ItemView.class));
            routes.add(new AuthorizedRoute("costing", "Costing", IngredientView.class));
        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("item", "Item Stock", ItemView.class));
            routes.add(new AuthorizedRoute("costing", "Costing", IngredientView.class));
            routes.add(new AuthorizedRoute("dashboard", "Dashboard", DashboardView.class));
        }
        return routes;
    }
}
