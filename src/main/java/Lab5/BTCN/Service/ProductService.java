package Lab5.BTCN.Service;

import Lab5.BTCN.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static int nextId = 1;
    private List<Product> listProduct = new ArrayList<>();

    public ProductService() {}

    public void add(Product newProduct) {
        newProduct.setId(nextId++);
        listProduct.add(newProduct);
    }

    public List<Product> GetAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void edit(Product editProduct) {
        Product find = listProduct.stream().filter(p -> p.getId() == editProduct.getId()).findFirst().orElse(null);
        if (find != null) {
            find.setName(editProduct.getName());
            if (editProduct.getImage() != null) {
                find.setImage(editProduct.getImage());
            }
            find.setPrice(editProduct.getPrice());
        }
    }

    public void delete(int id) {
        listProduct.removeIf(p -> p.getId() == id);
    }

    public List<Product> searchByName(String name) {
        return listProduct.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
