package es.rufino.kebab.configuration;

import es.rufino.kebab.models.*;
import es.rufino.kebab.repositories.PrivilegeRepository;
import es.rufino.kebab.services.CategoryService;
import es.rufino.kebab.services.ProductService;
import es.rufino.kebab.services.RoleService;
import es.rufino.kebab.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This will create example data in the database (products, roles, etc.).
 * This class gets automatically called on startup.
 *
 * @author ShinMugenNoKabe
 */
@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private Boolean alreadySetup = false;

    private final RoleService roleService;
    private final PrivilegeRepository privilegeRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        setupRoles();
        setupAdminUser();
        setupCategoriesAndProducts();

        alreadySetup = true;
    }

    private void setupRoles() {
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

        List<Privilege> administratorPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", administratorPrivileges);
    }

    @Transactional
    private void createRoleIfNotFound(String name, List<Privilege> privileges) {
        Role role = roleService.findByName(name);

        if (role != null) {
            return;
        }

        role = Role.builder()
                .name(name)
                .privileges(privileges)
                .build();

        roleService.insert(role);
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);

        if (privilege != null) {
            return privilege;
        }

        privilege = Privilege.builder()
                .name(name)
                .build();

        return privilegeRepository.save(privilege);
    }

    private void setupAdminUser() {
        Role adminRole = roleService.findByName("ROLE_ADMIN");

        User adminUser = User.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .firstName("Administrator")
                .lastName("Kebab Islas Canarias")
                .roles(Collections.singletonList(adminRole))
                .build();

        userService.register(adminUser);
    }

    private void setupCategoriesAndProducts() {
        Category doners = new Category("Doners");
        Category durums = new Category("Durums");
        Category lahmacuns = new Category("Lahmacuns");
        Category hamburguesas = new Category("Hamburguesas");
        Category platosCombinados = new Category("Platos combinados");
        Category raciones = new Category("Raciones");
        Category ensaladas = new Category("Ensaladas");
        Category bebidas = new Category("Bebidas");

        List<Category> categories = Arrays.asList(
                doners, durums, lahmacuns, hamburguesas, platosCombinados,
                raciones, ensaladas, bebidas
        );

        categoryService.insertMany(categories);

        // Insertado de products
        List<Product> products = Arrays.asList(
                new Product("Doner Kebab", 3.5, "donerkebab.jpg", 0.0, true, doners),
                new Product("Doner Kebab con queso", 4.0, "donerkebabqueso.jpg", 20.0, true, doners),
                new Product("Doner Kebab con maíz", 4.0, "donerkebabmaiz.jpg", 0.0, true, doners),
                new Product("Doner Kebab falafel", 3.5, "donerkebabfalafel.jpg", 50.0, true, doners),
                new Product("Durum Kebab", 4.5, "durumkebab.jpg", 0.0, true, durums),
                new Product("Durum Kebab con queso", 5.0, "durumkebabqueso.jpg", 15.0, false, durums),
                new Product("Durum Kebab con maíz", 5.0, "durumkebabmaiz.jpg", 0.0, true, durums),
                new Product("Durum Kebab doble", 5.5, "durumkebabdoble.jpg", 10.0, true, durums),
                new Product("Durum sólo carne", 5.5, "durumcarne.jpg", 0.0, true, durums),
                new Product("Durum Kebab falafel", 4.0, "durumkebabfalafel.jpg", 0.0, true, durums),
                new Product("Pizza turca", 5.0, "pizzaturca.jpg", 0.0, true, lahmacuns),
                new Product("Pizza turca con queso", 5.5, "pizzaturcaqueso.jpg", 0.0, true, lahmacuns),
                new Product("Pizza turca con maíz", 5.5, "pizzaturcamaiz.jpg", 0.0, false, lahmacuns),
                new Product("Pizza turca doble", 6.0, "pizzaturcadoble.jpg", 35.0, true, lahmacuns),
                new Product("Pizza turca sólo carne", 6.0, "pizzaturcacarne.jpg", 0.0, false, lahmacuns),
                new Product("Hamburguesa de ternera", 3.5, "hamburguesaternera.jpg", 0.0, true, hamburguesas),
                new Product("Hamburguesa de pollo", 3.5, "hamburguesapollo.jpg", 0.0, true, hamburguesas),
                new Product("Hamburguesa para niños", 2.5, "hamburguesaninos.jpg", 0.0, true, hamburguesas),
                new Product("Plato Doner Kebab", 6.0, "platodonerkebab.jpg", 70.0, true, platosCombinados),
                new Product("Plato Doner Kebab con arroz", 6.0, "platodonerkebabarroz.jpg", 0.0, false, platosCombinados),
                new Product("Plato Doner Kebab de la casa", 6.5, "platodonerkebabcasa.jpg", 25.0, true, platosCombinados),
                new Product("Patatas fritas", 1.5, "patatasfritas.jpg", 0.0, true, raciones),
                new Product("Nuggets de pollo (6 unds.)", 3.5, "nuggets.jpg", 0.0, true, raciones),
                new Product("Nuggets de pollo (9 unds.)", 6.0, "nuggets.jpg", 20.0, true, raciones),
                new Product("Aros de cebolla (6 unds.)", 3.5, "aroscebolla.jpg", 0.0, false, raciones),
                new Product("Calamares con patatas (6 unds.)", 4.5, "calamarespatatas.jpg", 0.0, true, raciones),
                new Product("Alitas de pollo (6 unds.)", 3.5, "alitas.jpg", 0.0, true, raciones),
                new Product("Alitas de pollo (9 unds.)", 6.0, "alitas.jpg", 0.0, true, raciones),
                new Product("Pedrata", 3.0, "pedrata.jpg", 0.0, false, raciones),
                new Product("Falafel (6 unds.)", 3.5, "falafel.jpg", 0.0, true, raciones),
                new Product("Samosa (1 unds.)", 1.0, "samosa.jpg", 0.0, true, raciones),
                new Product("Ensalada verde", 3.0, "ensaladaverde.jpg", 0.0, true, ensaladas),
                new Product("Ensalada turca", 4.0, "ensaladaturca.jpg", 20.0, true, ensaladas),
                new Product("Ensalada césar", 4.5, "ensaladacesar.jpg", 0.0, true, ensaladas),
                new Product("Coca-Cola", 1.0, "cocacola.jpg", 0.0, true, bebidas),
                new Product("Pepsi", 1.0, "pepsi.jpg", 0.0, true, bebidas),
                new Product("Agua", 1.0, "agua.jpg", 0.0, true, bebidas),
                new Product("Fanta naranja", 1.0, "fantanaranja.jpg", 0.0, true, bebidas),
                new Product("Fanta limón", 1.0, "fantalimon.jpg", 0.0, true, bebidas),
                new Product("Nestea", 1.0, "nestea.jpg", 0.0, true, bebidas),
                new Product("Aquarius", 1.0, "aquarius.jpg", 0.0, true, bebidas));

        productService.insertMany(products);
    }

}
