import java.util.*;
import java.io.*;

/* ================= PRODUCT ================= */

class Product{

    int id;
    String name;
    String category;
    double price;
    int stock;
    double rating=0;
    int ratingCount=0;

    Product(int id,String name,String category,double price,int stock){
        this.id=id;
        this.name=name;
        this.category=category;
        this.price=price;
        this.stock=stock;
    }

    void addRating(int r){
        rating=((rating*ratingCount)+r)/(ratingCount+1);
        ratingCount++;
    }

}

/* ================= CART ITEM ================= */

class CartItem{

    Product product;
    int qty;

    CartItem(Product p,int q){
        product=p;
        qty=q;
    }

}

/* ================= PRODUCT SERVICE ================= */

class ProductService{

    ArrayList<Product> products=new ArrayList<>();

    void loadProducts(){

        products.add(new Product(1,"Mobile","Electronics",15000,10));
        products.add(new Product(2,"Laptop","Electronics",55000,5));
        products.add(new Product(3,"Shoes","Fashion",2000,20));
        products.add(new Product(4,"Watch","Accessories",3000,15));
        products.add(new Product(5,"Headphones","Electronics",2500,12));

    }

    void viewProducts(){

        System.out.println("\nID Name Category Price Stock Rating");

        for(Product p:products){
            System.out.println(p.id+" "+p.name+" "+p.category+" "+p.price+" "+p.stock+" "+p.rating);
        }

    }

    void filterByCategory(String cat){

        for(Product p:products){
            if(p.category.equalsIgnoreCase(cat)){
                System.out.println(p.id+" "+p.name+" "+p.price);
            }
        }

    }

    void searchProduct(String name){

        for(Product p:products){
            if(p.name.equalsIgnoreCase(name)){
                System.out.println(p.id+" "+p.name+" "+p.price);
                return;
            }
        }

        System.out.println("Product not found");

    }

    void sortProducts(){

        products.sort(Comparator.comparingDouble(p->p.price));
        System.out.println("Sorted by price");
        viewProducts();

    }

    void addProduct(Product p){
        products.add(p);
        System.out.println("Product Added");
    }

    void updateProduct(int id,double price,int stock){

        for(Product p:products){
            if(p.id==id){
                p.price=price;
                p.stock=stock;
                System.out.println("Product Updated");
            }
        }

    }

    void lowStock(){

        System.out.println("\nLow Stock Products (<5)");

        for(Product p:products){
            if(p.stock<5){
                System.out.println(p.name+" Stock:"+p.stock);
            }
        }

    }

}

/* ================= WISHLIST ================= */

class WishlistService{

    ArrayList<Product> wishlist=new ArrayList<>();

    void add(ProductService ps,int id){

        for(Product p:ps.products){
            if(p.id==id){
                wishlist.add(p);
                System.out.println("Added to wishlist");
                return;
            }
        }

    }

    void view(){

        System.out.println("\n---- WISHLIST ----");

        for(Product p:wishlist){
            System.out.println(p.id+" "+p.name+" "+p.price);
        }

    }

}

/* ================= CART SERVICE ================= */

class CartService{

    ArrayList<CartItem> cart=new ArrayList<>();

    void addToCart(ProductService ps,int id,int q){

        for(Product p:ps.products){

            if(p.id==id){

                if(p.stock>=q){

                    cart.add(new CartItem(p,q));
                    p.stock-=q;

                    System.out.println("Added to cart");
                    return;

                }else{

                    System.out.println("Stock not available");

                }

            }

        }

    }

    void removeFromCart(int id){

        for(int i=0;i<cart.size();i++){

            if(cart.get(i).product.id==id){

                cart.remove(i);
                System.out.println("Item removed");
                return;

            }

        }

    }

    void updateQuantity(int id,int q){

        for(CartItem c:cart){

            if(c.product.id==id){
                c.qty=q;
                System.out.println("Quantity Updated");
            }

        }

    }

    void viewCart(){

        double total=0;

        System.out.println("\n----- CART -----");

        for(CartItem c:cart){

            double price=c.product.price*c.qty;
            System.out.println(c.product.name+" x "+c.qty+" = "+price);
            total+=price;

        }

        System.out.println("Total = "+total);

    }

}

/* ================= ORDER SERVICE ================= */

class OrderService{

    double revenue=0;
    int orders=0;

    void placeOrder(String user,CartService cs){

        Scanner sc=new Scanner(System.in);

        double total=0;

        for(CartItem c:cs.cart){
            total+=c.product.price*c.qty;
        }

        System.out.print("Enter coupon code (SAVE10 / SAVE20): ");
        String code=sc.next();

        if(code.equals("SAVE10")){
            total*=0.9;
        }

        if(code.equals("SAVE20")){
            total*=0.8;
        }

        System.out.println("\nSelect Payment Method");
        System.out.println("1 COD");
        System.out.println("2 UPI");
        System.out.println("3 Card");

        int pay=sc.nextInt();

        if(pay==1) System.out.println("Payment: COD");
        if(pay==2) System.out.println("Payment: UPI");
        if(pay==3) System.out.println("Payment: Card");

        System.out.println("\n------ BILL ------");

        for(CartItem c:cs.cart){
            System.out.println(c.product.name+" x "+c.qty);
        }

        System.out.println("Final Amount = "+total);

        revenue+=total;
        orders++;

        saveOrder(user,total);

        cs.cart.clear();

    }

    void saveOrder(String user,double total){

        try{

            FileWriter fw=new FileWriter("orders.txt",true);
            fw.write(user+" Order:"+total+"\n");
            fw.close();

        }catch(Exception e){}

    }

    void viewOrders(){

        try{

            BufferedReader br=new BufferedReader(new FileReader("orders.txt"));

            String line;

            while((line=br.readLine())!=null){
                System.out.println(line);
            }

        }catch(Exception e){
            System.out.println("No orders");
        }

    }

    void userOrders(String user){

        try{

            BufferedReader br=new BufferedReader(new FileReader("orders.txt"));

            String line;

            while((line=br.readLine())!=null){

                if(line.startsWith(user)){
                    System.out.println(line);
                }

            }

        }catch(Exception e){}

    }

    void salesReport(){

        System.out.println("\nTotal Orders: "+orders);
        System.out.println("Total Revenue: "+revenue);

    }

}

/* ================= USER SERVICE ================= */

class UserService{

    String currentUser="";

    void register(){

        try{

            Scanner sc=new Scanner(System.in);

            System.out.print("Enter username: ");
            String user=sc.next();

            System.out.print("Enter password: ");
            String pass=sc.next();

            FileWriter fw=new FileWriter("users.txt",true);

            fw.write(user+","+pass+"\n");

            fw.close();

            System.out.println("Registration Successful");

        }catch(Exception e){}

    }

    boolean login(){

        try{

            Scanner sc=new Scanner(System.in);

            System.out.print("Username: ");
            String u=sc.next();

            System.out.print("Password: ");
            String p=sc.next();

            BufferedReader br=new BufferedReader(new FileReader("users.txt"));

            String line;

            while((line=br.readLine())!=null){

                String data[]=line.split(",");

                if(data[0].equals(u)&&data[1].equals(p)){
                    currentUser=u;
                    return true;
                }

            }

        }catch(Exception e){}

        System.out.println("Invalid Login");

        return false;

    }

}

/* ================= ADMIN SERVICE ================= */

class AdminService{

    boolean adminLogin(){

        Scanner sc=new Scanner(System.in);

        System.out.print("Admin username: ");
        String u=sc.next();

        System.out.print("Admin password: ");
        String p=sc.next();

        if(u.equals("admin") && p.equals("1234")){
            return true;
        }

        System.out.println("Wrong credentials");

        return false;

    }

}

/* ================= MAIN ================= */

public class Main{

static Scanner sc=new Scanner(System.in);

public static void main(String args[]){

ProductService ps=new ProductService();
CartService cs=new CartService();
OrderService os=new OrderService();
UserService us=new UserService();
AdminService as=new AdminService();
WishlistService ws=new WishlistService();

ps.loadProducts();

int choice;

do{

System.out.println("\n===== ONLINE SHOP =====");

System.out.println("1 Register");
System.out.println("2 Login");
System.out.println("3 Admin Login");
System.out.println("4 Exit");

choice=sc.nextInt();

switch(choice){

case 1: us.register(); break;

case 2:
if(us.login()){
userMenu(ps,cs,os,us,ws);
}
break;

case 3:
if(as.adminLogin()){
adminMenu(ps,os);
}
break;

}

}while(choice!=4);

}

static void userMenu(ProductService ps,CartService cs,OrderService os,UserService us,WishlistService ws){

int ch;

do{

System.out.println("\n===== USER MENU =====");

System.out.println("1 View Products");
System.out.println("2 Search Product");
System.out.println("3 Filter Category");
System.out.println("4 Sort Products");
System.out.println("5 Add To Cart");
System.out.println("6 Remove From Cart");
System.out.println("7 Update Quantity");
System.out.println("8 View Cart");
System.out.println("9 Place Order");
System.out.println("10 My Orders");
System.out.println("11 Add Wishlist");
System.out.println("12 View Wishlist");
System.out.println("13 Rate Product");
System.out.println("14 Logout");

ch=sc.nextInt();

switch(ch){

case 1: ps.viewProducts(); break;

case 2:
System.out.print("Name:");
ps.searchProduct(sc.next());
break;

case 3:
System.out.print("Category:");
ps.filterByCategory(sc.next());
break;

case 4: ps.sortProducts(); break;

case 5:
System.out.print("ID:");
int id=sc.nextInt();
System.out.print("Qty:");
int q=sc.nextInt();
cs.addToCart(ps,id,q);
break;

case 6:
System.out.print("ID:");
cs.removeFromCart(sc.nextInt());
break;

case 7:
System.out.print("ID:");
int pid=sc.nextInt();
System.out.print("Qty:");
cs.updateQuantity(pid,sc.nextInt());
break;

case 8: cs.viewCart(); break;

case 9: os.placeOrder(us.currentUser,cs); break;

case 10: os.userOrders(us.currentUser); break;

case 11:
System.out.print("Product ID:");
ws.add(ps,sc.nextInt());
break;

case 12: ws.view(); break;

case 13:
System.out.print("Product ID:");
int rid=sc.nextInt();
System.out.print("Rating(1-5):");
int r=sc.nextInt();

for(Product p:ps.products){
if(p.id==rid){
p.addRating(r);
}
}
break;

}

}while(ch!=14);

}

static void adminMenu(ProductService ps,OrderService os){

int ch;

do{

System.out.println("\n===== ADMIN =====");

System.out.println("1 View Products");
System.out.println("2 Add Product");
System.out.println("3 Update Product");
System.out.println("4 View Orders");
System.out.println("5 Sales Report");
System.out.println("6 Low Stock");
System.out.println("7 Exit");

ch=sc.nextInt();

switch(ch){

case 1: ps.viewProducts(); break;

case 2:

System.out.print("ID:");
int id=sc.nextInt();

System.out.print("Name:");
String name=sc.next();

System.out.print("Category:");
String cat=sc.next();

System.out.print("Price:");
double price=sc.nextDouble();

System.out.print("Stock:");
int stock=sc.nextInt();

ps.addProduct(new Product(id,name,cat,price,stock));
break;

case 3:

System.out.print("ID:");
int pid=sc.nextInt();

System.out.print("Price:");
double pr=sc.nextDouble();

System.out.print("Stock:");
int st=sc.nextInt();

ps.updateProduct(pid,pr,st);
break;

case 4: os.viewOrders(); break;

case 5: os.salesReport(); break;

case 6: ps.lowStock(); break;

}

}while(ch!=7);

}

}