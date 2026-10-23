package com.example.smartpantry.data;

import android.content.*;import android.database.*;import android.database.sqlite.*;import com.example.smartpantry.model.Models.*;import java.util.*;

public class PantryDbHelper extends SQLiteOpenHelper {
    private static final String DB="smart_pantry.db"; private static final int VERSION=1;
    public PantryDbHelper(Context c){super(c,DB,null,VERSION);}
    public void onCreate(SQLiteDatabase db){ db.execSQL("CREATE TABLE pantry(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry TEXT)"); db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY,name TEXT NOT NULL,method TEXT NOT NULL)"); db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT,recipe_id INTEGER,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL)"); seed(db); }
    public void onUpgrade(SQLiteDatabase db,int oldV,int newV){}
    private void seed(SQLiteDatabase db){
        String[][] data={
        {"Tomato Pasta","Boil pasta. Saute garlic and tomatoes in olive oil. Toss with pasta and season.","pasta|200|g;tomato|2|count;garlic|1|clove;olive oil|1|tbsp"},
        {"Vegetable Omelette","Whisk eggs, fold in vegetables and cheese, then cook until set.","egg|2|count;onion|0.5|count;bell pepper|0.5|count;cheese|30|g"},
        {"Chickpea Salad","Combine chickpeas, cucumber, tomato and lemon. Dress and serve chilled.","chickpea|400|g;cucumber|1|count;tomato|1|count;lemon|0.5|count"},
        {"Banana Pancakes","Mash banana with eggs and flour. Cook small pancakes in a hot pan.","banana|2|count;egg|2|count;flour|120|g;milk|150|ml"},
        {"Creamy Mushroom Rice","Cook rice. Saute mushrooms and onion, add cream, then stir through rice.","rice|200|g;mushroom|200|g;onion|1|count;cream|100|ml"},
        {"Tuna Sandwich","Mix tuna with mayonnaise. Fill bread with tuna and lettuce.","tuna|1|can;bread|2|slice;mayonnaise|1|tbsp;lettuce|2|leaf"},
        {"Lentil Soup","Simmer lentils, carrot, onion and stock until tender. Season to taste.","lentil|200|g;carrot|2|count;onion|1|count;vegetable stock|500|ml"},
        {"Apple Crumble","Top sliced apples with a flour, butter and sugar crumble. Bake until golden.","apple|3|count;flour|100|g;butter|60|g;sugar|50|g"},
        {"Garlic Toast","Mix garlic with butter, spread on bread and toast until crisp.","bread|2|slice;garlic|2|clove;butter|30|g"},
        {"Greek Yogurt Bowl","Layer yogurt with banana, oats and honey.","yogurt|200|g;banana|1|count;oats|40|g;honey|1|tbsp"},
        {"Bean Quesadilla","Fill tortilla with beans, pepper and cheese. Fold and toast.","tortilla|2|count;bean|300|g;bell pepper|1|count;cheese|60|g"},
        {"Carrot Soup","Cook carrot and onion in stock until soft, blend smooth and finish with cream.","carrot|4|count;onion|1|count;vegetable stock|600|ml;cream|50|ml"},
        {"Pesto Pasta","Cook pasta and toss with pesto, tomato and parmesan.","pasta|200|g;pesto|3|tbsp;tomato|2|count;parmesan|30|g"},
        {"Fried Rice","Stir fry rice with egg, peas, carrot and soy sauce.","rice|300|g;egg|2|count;peas|100|g;carrot|1|count;soy sauce|1|tbsp"},
        {"Avocado Toast","Mash avocado with lemon and spread over toasted bread.","avocado|1|count;bread|2|slice;lemon|0.5|count"},
        {"Berry Smoothie","Blend berries, banana, yogurt and milk until smooth.","berries|150|g;banana|1|count;yogurt|100|g;milk|200|ml"},
        {"Potato Hash","Pan-fry potato with onion and pepper until golden.","potato|3|count;onion|1|count;bell pepper|1|count;olive oil|1|tbsp"},
        {"Couscous Bowl","Prepare couscous and mix with chickpeas, cucumber and lemon.","couscous|200|g;chickpea|200|g;cucumber|1|count;lemon|0.5|count"}
        };
        for(int i=0;i<data.length;i++){ContentValues r=new ContentValues();r.put("id",i+1);r.put("name",data[i][0]);r.put("method",data[i][1]);db.insert("recipes",null,r);for(String part:data[i][2].split(";")){String[] p=part.split("\\|");ContentValues x=new ContentValues();x.put("recipe_id",i+1);x.put("name",p[0]);x.put("quantity",Double.parseDouble(p[1]));x.put("unit",p[2]);db.insert("recipe_ingredients",null,x);}}
    }
    public List<PantryItem> getPantry(){List<PantryItem> out=new ArrayList<>();Cursor c=getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,COALESCE(expiry,'') FROM pantry ORDER BY name",null);while(c.moveToNext())out.add(new PantryItem(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4)));c.close();return out;}
    public long savePantry(PantryItem item){ContentValues v=new ContentValues();v.put("name",item.name);v.put("quantity",item.quantity);v.put("unit",item.unit);v.put("expiry",item.expiry);if(item.id==0)return getWritableDatabase().insert("pantry",null,v);getWritableDatabase().update("pantry",v,"id=?",new String[]{String.valueOf(item.id)});return item.id;}
    public void deletePantry(long id){getWritableDatabase().delete("pantry","id=?",new String[]{String.valueOf(id)});}
    public Recipe getRecipe(int id){Cursor c=getReadableDatabase().rawQuery("SELECT name,method FROM recipes WHERE id=?",new String[]{String.valueOf(id)});if(!c.moveToFirst()){c.close();return null;}Recipe r=new Recipe(id,c.getString(0),c.getString(1));c.close();Cursor i=getReadableDatabase().rawQuery("SELECT name,quantity,unit FROM recipe_ingredients WHERE recipe_id=?",new String[]{String.valueOf(id)});while(i.moveToNext())r.ingredients.add(new RecipeIngredient(i.getString(0),i.getDouble(1),i.getString(2)));i.close();return r;}
    public List<Recipe> getStrictMatches(){Map<String,Double> have=new HashMap<>();Map<String,String> units=new HashMap<>();for(PantryItem p:getPantry()){String n=normalize(p.name);have.put(n,have.containsKey(n)?have.get(n)+toBase(p.quantity,p.unit):toBase(p.quantity,p.unit));units.put(n,p.unit);}List<Recipe> out=new ArrayList<>();Cursor c=getReadableDatabase().rawQuery("SELECT id FROM recipes ORDER BY name",null);while(c.moveToNext()){Recipe r=getRecipe(c.getInt(0));boolean ok=true;for(RecipeIngredient x:r.ingredients){String n=normalize(x.name);if(!have.containsKey(n)||!enough(have.get(n),x.quantity,x.unit,units.get(n))){ok=false;break;}}if(ok)out.add(r);}c.close();return out;}
    public static String normalize(String s){s=s.toLowerCase(Locale.US).trim().replaceAll("[^a-z0-9 ]","");if(s.endsWith("ies"))s=s.substring(0,s.length()-3)+"y";else if(s.endsWith("es")&&s.length()>3)s=s.substring(0,s.length()-2);else if(s.endsWith("s")&&s.length()>2)s=s.substring(0,s.length()-1);return s;}
    private static double toBase(double q,String u){u=u.toLowerCase(Locale.US);if(u.equals("kg"))return q*1000;if(u.equals("l"))return q*1000;return q;}
    private static boolean enough(double have,double need,String needUnit,String haveUnit){String n=needUnit.toLowerCase(Locale.US),h=haveUnit.toLowerCase(Locale.US);if((n.equals("g")||n.equals("kg"))&&(h.equals("g")||h.equals("kg")))return have>=toBase(need,n);if((n.equals("ml")||n.equals("l"))&&(h.equals("ml")||h.equals("l")))return have>=toBase(need,n);return have>=need && (n.equals(h)||n.equals("count")||h.equals("count"));}
}
