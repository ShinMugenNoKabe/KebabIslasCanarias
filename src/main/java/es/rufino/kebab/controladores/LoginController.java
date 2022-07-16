/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.controladores;


import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.RolRepository;
import es.rufino.kebab.servicios.UsuarioServicio;
import es.rufino.kebab.upload.StorageService;
import java.util.Arrays;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Controller
public class LoginController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private StorageService storageService;
    
    @Autowired
    private RolRepository rolRepositorio;

    @GetMapping("/iniciar-sesion")
    public String vistaLogin(Model model) {
        model.addAttribute("usuario", new Usuario());

        return "iniciar-sesion";
    }
    
    @GetMapping("/registro")
    public String vistaRegister(Model model) {
        model.addAttribute("usuario", new Usuario());

        return "registro";
    }

    /**
     * Registra un nuevo usuario, introducido en el formulario de registro.
     * Comprueba todos las validaciones de los atributos de la clase, además
     * de que no exista ya un usuario con ese e-mail.
     * @param nuevoUsuario Usuario a regisrar
     * @param result Objeto de validaciones de Spring Boot
     * @return Formulario de login si se ha registrado correctamente o formulario
     * de registro si falla la validación
     */
    @PostMapping("/auth/register")
    public String register(@Valid Usuario nuevoUsuario, BindingResult result) {
        // Comprobación del e-mail
        if (usuarioServicio.buscarPorEmail(nuevoUsuario.getEmail()) != null) {
            result.rejectValue("email", "error.user",
                    "Ya existe un usuario registrado con la dirección de correo electrónico introducido.");
        }
        
        if (result.hasErrors()) {
            return "registro";
        }
        
        nuevoUsuario.setListaDeRoles(Arrays.asList(rolRepositorio.findByNombre("ROLE_USER")));
        nuevoUsuario.setEnabled(true);
        usuarioServicio.registrar(nuevoUsuario);

        return "redirect:/login";
    }
    
}
