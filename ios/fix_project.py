#!/usr/bin/env python3
import re

project_file = "Runner.xcodeproj/project.pbxproj"

with open(project_file, 'r') as f:
    content = f.read()

# Add to Runner group (after AppDelegate.swift line)
content = re.sub(
    r'(74858FAE1ED2DC5600515810 /\* AppDelegate\.swift \*/,)\n',
    r'\1\n\t\t\t\t74858FB11ED2DC5600515810 /* SimaHandler.swift */,\n',
    content
)

# Add to Sources build phase (after AppDelegate.swift in Sources)
content = re.sub(
    r'(74858FAF1ED2DC5600515810 /\* AppDelegate\.swift in Sources \*/,)\n',
    r'\1\n\t\t\t\t74858FB01ED2DC5600515810 /* SimaHandler.swift in Sources */,\n',
    content
)

with open(project_file, 'w') as f:
    f.write(content)

print("Project file updated successfully!")

