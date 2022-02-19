package com.smart.inventory.application.views;

import com.smart.inventory.application.data.services.owner.OwnerService;
import com.smart.inventory.application.util.Utilities;
import com.smart.inventory.application.views.session.LogoutView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {


    private final OwnerService ownerService;

    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-s");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                // Use Lumo classnames for suitable font size and margin
                addClassNames("me-s", "text-l");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private H1 viewTitle;


    public MainLayout(OwnerService ownerService) {
        this.ownerService = ownerService;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        return header;
    }


    private Component createHeaderTitle() {

        String text = "";
        if (Utilities.owner != null) {
            text = Utilities.owner.getRoles().getRoleName();
        }

        if (Utilities.employer != null) {
            text = Utilities.employer.getRoles().getRoleName();
        }

        var sub = new Label("(Smart Inventory)");
        sub.addClassName("sub");
        sub.addClassNames("m-0", "px-m");


        var role = new Label(text);
        role.addClassName("role");
        role.setHeight("12px");
        role.addClassNames("m-0", "px-m");

        H3 cmpName = new H3(Utilities.company.getName());
        cmpName.addClassName("secTitle");
        cmpName.addClassNames("m-0", "px-m");


        VerticalLayout layout = new VerticalLayout(cmpName, sub);
        layout.setWidthFull();
        layout.addClassNames("flex", "my-s", "px-m", "py-xs");
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setHeight("3.7em");


        return layout;
    }

    private Component createDrawerContent() {
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(createHeaderTitle(),
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "max-h-full", "min-h-full", "overflow-auto");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0", "text-l");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }
        return nav;
    }

    private MenuItemInfo[] createMenuItems() {

        if (Utilities.owner != null) {
            return ownerService.getAuthorizedRoutes(Utilities.owner.getRoles()).stream()
                    .map(authorizedRoute ->
                            new MenuItemInfo(authorizedRoute.name(),
                                    "", authorizedRoute.view()))
                    .toArray(MenuItemInfo[]::new);
        }
        return ownerService.getAuthorizedRoutes(Utilities.employer.getRoles()).stream()
                .map(authorizedRoute ->
                        new MenuItemInfo(authorizedRoute.name(),
                                "", authorizedRoute.view()))
                .toArray(MenuItemInfo[]::new);
    }

    private Footer createFooter() {
        Button logout = new Button("Log out");
        logout.addClickListener(buttonClickEvent -> new LogoutView());
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Footer layout = new Footer(logout);
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
