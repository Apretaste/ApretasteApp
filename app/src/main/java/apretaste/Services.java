package apretaste;

import java.text.Collator;

/**
 * Created by  on 7/10/2017.
 */

public class Services {
    public String name;
    public String icon;
    public String used;
    public String id;
    public String description;
    public String creator;
    public String updated;
    public String fav;
    public String category;

    public void setFav(String fav) {
        this.fav = fav;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public void setDescription(String decription) {
        this.description = decription;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getUsed() {
        return used;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getCreator() {
        return creator;
    }

    public String getUpdated() {
        return updated;
    }

    public String getFav() {
        return fav;
    }

    @Override
    public boolean  equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Services service = (Services) object;
            if (this.name.equals(service.getName())) {
                result = true;
            }
        }
        return result;
    }
}
