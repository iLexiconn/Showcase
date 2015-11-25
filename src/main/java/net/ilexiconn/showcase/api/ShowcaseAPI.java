package net.ilexiconn.showcase.api;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.showcase.api.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Beta
public class ShowcaseAPI {
    public static final LoggerHelper logger = new LoggerHelper("ShowcaseAPI");

    public static final String VERSION = "0.2.0-develop";

    private static List<IModel> modelList = Lists.newArrayList();
    private static Map<String, IModelParser<? extends IModel>> modelTypeMap = Maps.newHashMap();
    private static ModelFallbackParser fallbackParser = new ModelFallbackParser();
    private static IFallbackModel fallbackModel;

    public static void setFallbackModel(IFallbackModel fallback) {
        fallbackModel = fallback;
    }

    public static IFallbackModel getFallbackModel() {
        return fallbackModel;
    }

    public static <T extends IModel> void registerModelParser(String name, IModelParser<T> type) {
        if (hasModelParser(name)) {
            throw new DuplicateModelsException();
        } else {
            modelTypeMap.put(name, type);
        }
    }

    public static List<IModelParser<? extends IModel>> getModelParserList() {
        return Lists.newArrayList(modelTypeMap.values());
    }

    public static boolean hasModelParser(String name) {
        for (Map.Entry<String, IModelParser<? extends IModel>> entry : modelTypeMap.entrySet()) {
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
            return modelTypeMap.get(name);
        }
    }

    public static IModelParser<? extends IModel> getModelParserFor(IModel model) {
        if (model != null) {
            if (model instanceof IFallbackModel) {
                return fallbackParser;
            } else {
                return getModelParserFor(model.getClass());
            }
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
        return modelList;
    }

    public static IModel getModel(String name) {
        for (IModel model : modelList) {
            if (model.getName().equals(name)) {
                return model;
            }
        }
        return fallbackModel;
    }

    public static IModel getModel(int index) {
        if (index < 0 || index > modelList.size()) {
            return fallbackModel;
        } else {
            return modelList.get(index);
        }
    }

    public static int getModelIndex(IModel model) {
        for (IModel m : modelList) {
            if (model == m) {
                return modelList.indexOf(m);
            }
        }
        return -1;
    }

    public static int getModelCount() {
        int i = 0;
        for (IModel model : modelList) {
            if (model != null) {
                i++;
            }
        }
        return i;
    }

    public static void reloadModels() {
        modelList.clear();
        for (IModelParser<?> modelParser : getModelParserList()) {
            for (File dir : modelParser.getDirectories()) {
                if (dir == null) {
                    return;
                } else if (!dir.exists()) {
                    return;
                } else if (!dir.isDirectory()) {
                    return;
                } else {
                    for (File file : dir.listFiles()) {
                        if (file.getName().endsWith(modelParser.getExtension())) {
                            try {
                                ShowcaseAPI.logger.debug("Parsing model file " + file.getName() + " with parser " + modelParser.getClass().getName());
                                modelList.add(modelParser.parse(file));
                            } catch (IOException e) {
                                ShowcaseAPI.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to read model file " + file.getName()));
                            }
                        }
                    }
                }
            }
        }
    }
}
