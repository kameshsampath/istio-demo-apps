SHELL := bash
CURRENT_DIR := $(PWD)
ENV_FILE := $(CURRENT_DIR)/.envrc

.PHONY:	clean
clean:
	. mvnw clean

.PHONY: all
all:	customer	preference	recommendation

.PHONY:	customer
customer:	clean
		# build and push hotspot image
		. mvnw package \
      	  -Dquarkus.container-image.build=true \
          -Dquarkus.container-image.push=true \
          -f $(CURRENT_DIR)/customer/pom.xml;
    # build and push native image
	  . mvnw -Pnative -Dquarkus.native.container-build=true \
	      -Dquarkus.container-image.build=true \
	      -Dquarkus.container-image.push=true \
	      package -f $(CURRENT_DIR)/customer/pom.xml;

.PHONY:	preference
preference:	clean
		# build and push hotspot image
		. mvnw package \
      	  -Dquarkus.container-image.build=true \
          -Dquarkus.container-image.push=true \
          -f $(CURRENT_DIR)/preference/pom.xml;
    # build and push native image
	  . mvnw -Pnative -Dquarkus.native.container-build=true \
	      -Dquarkus.container-image.build=true \
	      -Dquarkus.container-image.push=true \
	      package -f $(CURRENT_DIR)/preference/pom.xml;

.PHONY:	recommendation
recommendation:	clean
		# build and push hotspot image
		. mvnw package \
      	  -Dquarkus.container-image.build=true \
          -Dquarkus.container-image.push=true \
          -f $(CURRENT_DIR)/recommendation/pom.xml;
    # build and push native image
	  . mvnw -Pnative -Dquarkus.native.container-build=true \
	      -Dquarkus.container-image.build=true \
	      -Dquarkus.container-image.push=true \
	      package -f $(CURRENT_DIR)/recommendation/pom.xml;


