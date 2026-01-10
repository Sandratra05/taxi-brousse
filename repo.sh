#!/bin/bash

ENTITY_DIR="src/main/java/com/brousse/model"
REPO_DIR="src/main/java/com/brousse/repository"
SERVICE_DIR="src/main/java/com/brousse/service"

mkdir -p "$REPO_DIR"
mkdir -p "$SERVICE_DIR"

for file in "$ENTITY_DIR"/*.java; do
    entity=$(basename "$file" .java)
    # Repository
    cat > "$REPO_DIR/${entity}Repository.java" <<EOF
package com.brousse.repository;

import com.brousse.model.${entity};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${entity}Repository extends JpaRepository<${entity}, Integer> {
}
EOF
done