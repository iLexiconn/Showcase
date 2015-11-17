package net.ilexiconn.showcase.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class ShowcaseRegistry {
    private static Map<String, IModelParser<? extends IModel>> modelTypeList = Maps.newHashMap();

    public static <T extends IModel> void registerModelParser(String name, IModelParser<T> type) {
        if (hasModelParser(name)) {
            throw new DuplicateModelsException();
        } else {
            modelTypeList.put(name, type);
        }
    }

    public static List<IModelParser<? extends IModel>> getModelParserList() {
        return Lists.newArrayList(modelTypeList.values());
    }

    public static boolean hasModelParser(String name) {
        for (Map.Entry<String, IModelParser<? extends IModel>> entry : modelTypeList.entrySet()) {
            if (entry.getKey().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static IModelParser<? extends IModel> getModelParser(String name) {
        if (!hasModelParser(name)) {
            throw new ModelParserNotFoundException();
        } else {
            return modelTypeList.get(name);
        }
    }

    public static  IModelParser<? extends IModel> getModelParserFor(IModel model) {
        if (model != null) {
            return getModelParserFor(model.getClass());
        } else {
            return null;
        }
    }

    public static IModelParser<? extends IModel> getModelParserFor(Class<? extends IModel> model) {
        for (IModelParser<? extends IModel> parser : getModelParserList()) {
            if (parser.getModelClass() == model) {
                return parser;
            }
        }
        return null;
    }

    public static List<IModel> getModelList() {
        List<IModel> modelList = Lists.newArrayList();
        for (IModelParser<? extends IModel> parser : getModelParserList()) {
            for (IModel model : parser.getModelList()) {
                modelList.add(model);
            }
        }
        return modelList;
    }

    public static IModel getModel(String name) {
        for (IModelParser<? extends IModel> parser : getModelParserList()) {
            for (IModel model : parser.getModelList()) {
                if (model.getName().equals(name)) {
                    return model;
                }
            }
        }
        return null;
    }

    public static int getModelCount() {
        int i = 0;
        for (IModelParser<? extends IModel> parser : getModelParserList()) {
            for (IModel model : parser.getModelList()) {
                if (model != null) {
                    i++;
                }
            }
        }
        return i;
    }
}
