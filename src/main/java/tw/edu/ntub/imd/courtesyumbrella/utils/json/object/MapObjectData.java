package tw.edu.ntub.imd.courtesyumbrella.utils.json.object;

import com.fasterxml.jackson.databind.JsonNode;
import tw.edu.ntub.imd.courtesyumbrella.utils.function.AddObjectDataMapConsumer;
import tw.edu.ntub.imd.courtesyumbrella.utils.json.ResponseData;
import tw.edu.ntub.imd.courtesyumbrella.utils.json.array.ArrayData;
import tw.edu.ntub.imd.utils.function.TripleConsumer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class MapObjectData implements ResponseData {
    private ObjectData objectData;

    public MapObjectData() {
        objectData = new ObjectData();
    }

    protected MapObjectData(ObjectData objectData) {
        this.objectData = objectData;
    }

    public MapObjectData addArray(String key, Map<String, String> resource) {
        return addArray(key, resource, (objectData, k, v) -> {
            objectData.add("code", k);
            objectData.add("value", v);
        });
    }

    public <K, V> MapObjectData addArray(String key, Map<K, V> resource, TripleConsumer<ObjectData, K, V> addObjectDataConsumer) {
        objectData.addArray(key).addMapToThis().add(resource, addObjectDataConsumer);
        return this;
    }

    public <K, V> MapObjectData addArray(String key, Map<K, V> resource, AddObjectDataMapConsumer<K, V> addObjectDataMapConsumer) {
        objectData.addArray(key).addMapToThis().add(resource, addObjectDataMapConsumer);
        return this;
    }

    public <K, V> BiConsumer<K, V> addArrayAndAddObjectToNewArray(String key, TripleConsumer<ObjectData, K, V> addObjectDataConsumer) {
        ArrayData newArrayData = objectData.addArray(key);
        return (k, v) -> addObjectDataConsumer.accept(newArrayData.addObject(), k, v);
    }

    public <K, V> BiConsumer<K, V> addArrayAndAddObjectToNewArray(String key, AddObjectDataMapConsumer<K, V> addObjectDataMapConsumer) {
        ArrayData newArrayData = objectData.addArray(key);
        AtomicInteger index = new AtomicInteger();
        return (k, v) -> addObjectDataMapConsumer.addObject(newArrayData.addObject(), index.getAndIncrement(), k, v);
    }

    @Override
    public JsonNode getData() {
        return objectData.getData();
    }
}
