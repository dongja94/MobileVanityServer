package com.mobilevanity.backend;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.mobilevanity.backend.common.InvalidUserInfoException;
import com.mobilevanity.backend.common.UserAddException;
import com.mobilevanity.backend.common.Utility;
import com.mobilevanity.backend.data.BeautyTip;
import com.mobilevanity.backend.data.Brand;
import com.mobilevanity.backend.data.Comment;
import com.mobilevanity.backend.data.Cosmetic;
import com.mobilevanity.backend.data.CosmeticItem;
import com.mobilevanity.backend.data.FAQ;
import com.mobilevanity.backend.data.Notify;
import com.mobilevanity.backend.data.Product;
import com.mobilevanity.backend.data.Sale;
import com.mobilevanity.backend.data.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Administrator on 2016-08-24.
 */
public class DataManager {
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    private DataManager() {
        ObjectifyService.register(User.class);
        ObjectifyService.register(BeautyTip.class);
        ObjectifyService.register(Brand.class);
        ObjectifyService.register(FAQ.class);
        ObjectifyService.register(Notify.class);
        ObjectifyService.register(Product.class);
        ObjectifyService.register(Cosmetic.class);
        ObjectifyService.register(CosmeticItem.class);
        ObjectifyService.register(Comment.class);
        ObjectifyService.register(Sale.class);
    }

    public User addUser(User user) throws UserAddException {
        User alreadyUser = getUserByEmail(user.email);
        if (alreadyUser == null) {
            ofy().save().entity(user).now();
            return user;
        }
        throw new UserAddException("User exist");
    }

    public User getUserByEmail(String email) {
        try {
            User user = ofy().load().type(User.class).filter("email", email).first().now();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User login(String email, String password) throws InvalidUserInfoException {
        User user = ofy().load().type(User.class).filter("email", email).filter("password",password).first().now();
        if (user != null) {
            return user;
        }
        throw new InvalidUserInfoException("email or password invalid");
    }

    public User saveUser(User user) {
        ofy().save().entity(user).now();
        return user;
    }

    public User getUserByFacebookId(String facebookId) {
        User user = ofy().load().type(User.class).filter("facebookId", facebookId).first().now();
        return user;
    }

    public User getUserById(long id) {
        try {
            User user = ofy().load().type(User.class).id(id).now();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CosmeticItem> findCosmeticItem(User user, int category, int item) {
        Query query = ofy().load().type(Product.class).filter("category", category);
        if (item > 0) {
            query = query.filter("item", item);
        }
        List<Product> products = query.list();
        List<Cosmetic> cosmetics = ofy().load().type(Cosmetic.class).filter("product in", products).list();
        List<CosmeticItem> cosmeticItems = ofy().load().type(CosmeticItem.class).filter("owner", user).filter("cosmetic in", cosmetics).list();
        return cosmeticItems;
    }

    public CosmeticItem saveCosmeticItem(CosmeticItem item) {
        ofy().save().entity(item).now();
        return item;
    }

    public boolean deleteCosmeticItem(CosmeticItem item) {
        ofy().delete().entity(item).now();
        return true;
    }

    public CosmeticItem getCosmeticItem(long id) {
        return ofy().load().type(CosmeticItem.class).id(id).now();
    }

    public Cosmetic findCosmeticByBarcode(String barcode) {
        Cosmetic cosmetic = ofy().load().type(Cosmetic.class).filter("barcode", barcode).first().now();
        return cosmetic;
    }

    public List<Cosmetic> findCosmetic(Brand brand, int category, int item, String keyword) {
        List<Brand> acceptBrands = new ArrayList<>();
        if (!Utility.isEmpty(keyword)) {
            List<Brand> blist = ofy().load().type(Brand.class).list();
            for (Brand b : blist) {
                if (b.name.contains(keyword)) {
                    acceptBrands.add(b);
                }
            }
        }
        Query query = ofy().load().type(Product.class).order("brand");
        if (brand != null) {
            query = query.filter("brand", brand);
        }
        if (category > 0) {
            query = query.filter("category", category);
        }
        if (item > 0) {
            query = query.filter("item", item);
        }
        List<Product> products = query.list();
        List<Product> acceptProducts = ofy().load().type(Product.class).order("brand").filter("brand in", acceptBrands).list();
        if (!Utility.isEmpty(keyword)) {
            for (Product p : products) {
                if (p.name.contains(keyword)) {
                    if (!acceptProducts.contains(p)) {
                        acceptProducts.add(p);
                    }
                }
            }
        }
        List<Cosmetic> acceptCosmetics = ofy().load().type(Cosmetic.class).order("product").filter("product in", acceptProducts).list();
        List<Cosmetic> cosmetics = ofy().load().type(Cosmetic.class).filter("product in", products).list();
        if (!Utility.isEmpty(keyword)) {
            for (Cosmetic c : cosmetics) {
                if (c.colorName.contains(keyword)) {
                    if (!acceptCosmetics.contains(c)) {
                        acceptCosmetics.add(c);
                    }
                }
            }
        } else {
            acceptCosmetics.addAll(cosmetics);
        }
        return acceptCosmetics;
    }

    public Cosmetic saveCosmetic(Cosmetic cosmetic) {
        ofy().save().entity(cosmetic).now();
        return cosmetic;
    }

    public List<Cosmetic> listCosmetic(Product product) {
        if (product == null) {
            return ofy().load().type(Cosmetic.class).order("product").list();
        } else {
            return ofy().load().type(Cosmetic.class).filter("product", product).order("product").list();
        }
    }

    public Cosmetic getCosmetic(long id) {
        return ofy().load().type(Cosmetic.class).id(id).now();
    }

    public List<Sale> findSaleByProduct(Product product) {
        return ofy().load().type(Sale.class).filter("product", product).list();
    }

    public List<Sale> findSaleByDate(Date start, Date end) {
        Query query = ofy().load().type(Sale.class).order("startDay");
        if (start != null) {
            query = query.filter("startDay >", start);
        }
        if (end != null) {
            query = query.filter("endDay <", end);
        }
        return query.list();
    }

    public Sale getSale(long id) {
        return ofy().load().type(Sale.class).id(id).now();
    }

    public List<BeautyTip> findBeautyTip(User user, int sort, String keyword) {
        Query query = null;
        if (sort == BeautyTip.SORT_TYPE_RECENT) {
            query = ofy().load().type(BeautyTip.class).order("writeDate");
        } else {
            query = ofy().load().type(BeautyTip.class).order("likeCount");
        }
        if (user != null) {
            query = query.filter("user",user);
        }
        List<BeautyTip> list = query.list();
        if (!Utility.isEmpty(keyword)) {
            List<BeautyTip> newList = new ArrayList<>();
            for (BeautyTip bt : list) {
                if (bt.title.contains(keyword) || bt.content.contains(keyword)) {
                    newList.add(bt);
                }
            }
            list = newList;
        }
        return list;
    }

    public List<BeautyTip> findLikeBeautyTip(User user) {
        return ofy().load().type(BeautyTip.class).order("writeDate").filter("likUsers", user).list();
    }

    public BeautyTip saveBeautyTip(BeautyTip beautyTip) {
        beautyTip.likeCount = beautyTip.likeUsers.size();
        ofy().save().entity(beautyTip).now();
        return beautyTip;
    }

    public boolean deleteBeautyTip(BeautyTip beautyTip) {
        ofy().delete().entity(beautyTip).now();
        return true;
    }

    public BeautyTip getBeautyTip(long id) {
        return ofy().load().type(BeautyTip.class).id(id).now();
    }

    public List<Comment> findComment(BeautyTip beautyTip) {
        return ofy().load().type(Comment.class).order("writeDate").filter("beautyTip", beautyTip).list();
    }

    public List<Comment> findComment(User user) {
        return ofy().load().type(Comment.class).order("writeDate").filter("writer",user).list();
    }

    public Comment saveComment(Comment comment) {
        ofy().save().entity(comment).now();
        return comment;
    }

    public boolean deleteComment(Comment comment) {
        ofy().delete().entity(comment).now();
        return true;
    }

    public boolean deleteCommentAll(List<Comment> comments) {
        ofy().delete().entities(comments).now();
        return true;
    }


    public Comment getComment(long id) {
        return ofy().load().type(Comment.class).id(id).now();
    }

    public List<FAQ> listFAQ() {
        return ofy().load().type(FAQ.class).list();
    }

    public FAQ saveFAQ(FAQ faq) {
        ofy().save().entity(faq).now();
        return faq;
    }

    public FAQ getFAQ(long id) {
        return ofy().load().type(FAQ.class).id(id).now();
    }

    public List<Notify> findNotify(Date date) {
        return ofy().load().type(Notify.class).order("date").filter("date >", date).list();
    }

    public Notify saveNotify(Notify notify) {
        ofy().save().entity(notify).now();
        return notify;
    }

    public Notify getNotify(long id) {
        return ofy().load().type(Notify.class).id(id).now();
    }

    public List<Brand> listBrand() {
        return ofy().load().type(Brand.class).list();
    }

    public Brand findBrand(String name) {
        return ofy().load().type(Brand.class).filter("name",name).first().now();
    }

    public Brand getBrand(long id) {
        return ofy().load().type(Brand.class).id(id).now();
    }

    public Product saveProduct(Product product) {
        ofy().save().entity(product).now();
        return product;
    }

    public Product findProduct(String code) {
        return ofy().load().type(Product.class).filter("code",code).first().now();
    }

    public List<Product> listProduct(Brand brand) {
        if (brand == null) {
            return ofy().load().type(Product.class).order("brand").list();
        } else {
            return ofy().load().type(Product.class).filter("brand",brand).order("brand").list();
        }
    }

    public Product getProduct(long id) {
        return ofy().load().type(Product.class).id(id).now();
    }

    public Brand saveBrand(Brand brand) {
        ofy().save().entity(brand).now();
        return brand;
    }
}
