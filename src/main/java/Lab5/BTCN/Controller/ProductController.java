package Lab5.BTCN.Controller;

import Lab5.BTCN.Model.Product;
import Lab5.BTCN.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        return "create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") Product newProduct,
                         @RequestParam("imageProduct") MultipartFile imageProduct,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "create";
        }

        // Save image to static/images folder
        if (imageProduct != null && !imageProduct.isEmpty()) {
            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                String newImageFile = UUID.randomUUID() + ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listproduct", productService.GetAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productService.get(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("product") Product editProduct,
                       @RequestParam("imageProduct") MultipartFile imageProduct,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            return "edit";
        }

        if (imageProduct != null && !imageProduct.isEmpty()) {
            try {
                File saveFile = new ClassPathResource("static/images").getFile();
                String newImageFile = UUID.randomUUID() + ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                editProduct.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        productService.edit(editProduct);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String search(@RequestParam("name") String name, Model model) {
        List<Product> products = productService.searchByName(name);
        model.addAttribute("listproduct", products);
        return "index";
    }
}
