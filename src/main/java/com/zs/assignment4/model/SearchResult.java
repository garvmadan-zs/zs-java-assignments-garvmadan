package zs.assignment4.model;

public class SearchResult {
    private final String type;
    private final String content;
    private final String hierarchy;
    private final boolean fromCache;

    public SearchResult(String type, String content, String hierarchy, boolean fromCache) {
        this.type = type;
        this.content = content;
        this.hierarchy = hierarchy;
        this.fromCache = fromCache;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public boolean isFromCache() {
        return fromCache;
    }
}
