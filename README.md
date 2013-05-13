## Wicket-test-utils
The package contains test utilities aimed at Wicket projects.

### `Selection`

An easy way to find Wicket components by using Hamcrest matchers. Examples below.

```java
// All visible labels in page
List<Label> = select(Label.class).that(visibleInHierarchy()).from(page);

// The first WMC with non-null model object and link as a child
WebMarkupContainer wmc = select(WebMarkupContainer.class)
                          .that(hasChild(instanceOf(Link.class)))
                          .that(hasModelObject(notNullValue())
                          .firstFrom(page);

// Any component with id "foo"
Component c = selectWithId("foo").firstFrom(page);

// Component or null
Component c = selectWithId("foo").firstOrNullFrom(page);
```

### `MockStringResourceLoader`

Configurable, but by default returns the key as the loaded resource. Speeds up unit tests and makes them locale-independent.

Usage:
```java
public class TestApplication extends WebApplication {
    @Override
    protected void init() {
        super.init();
        this.getResourceSettings().getStringResourceLoaders().clear();
        this.getResourceSettings().getStringResourceLoaders().add(mockStringResourceLoader);
    }
}

mockStringResourceLoader.expectStringMessage("field.Required", "The field ${label} is required");
```


### `MockitoUtil`

Not a Wicket-specific helper as such, but facilitates Mockito testing. Improved generics of `ArgumentCaptor` and `argThat` compatible with Hamcrest 1.3

```java
// Not possible without unchecked warning with ArgumentCaptor.forClass(...)
ArgumentCaptor<List<String>> captor = MockitoUtil.captorFor(List.class);
```
