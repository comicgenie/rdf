package org.comicwiki.gcd;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.comicwiki.model.OrganizationType;

public class OrgLookupService {

	public static OrganizationType getOrganizationType(String text) {
		String lc = text.trim().toLowerCase();
		for (OrganizationType orgType : OrganizationType.values()) {
			if (lc.contains(orgType.name())) {
				return orgType;
			}
		}
		return null;
	}

	private List<String> organizationList = new ArrayList<>();

	public OrgLookupService() {
	}

	public OrgLookupService(List<String> organizations) {
		this.organizationList = organizations;
	}

	public boolean isOrganization(String name) {
		String text = name.trim();
		String noDots = text.replace("[.]", "");
		return getOrganizationType(name) != null
				|| organizationList.contains(text)
				|| organizationList.contains("The " + text)
				|| organizationList.contains(noDots)
				|| organizationList.contains("The " + noDots);
	}

	public void load(File resourceDir) throws IOException {
		try {
			organizationList = Files.readAllLines(new File(resourceDir,
					"MarvelOrganizations.txt").toPath(), Charset
					.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
