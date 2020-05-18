package app.coronawarn.server.services.distribution.assembly.diagnosiskeys.structure.directory;

import app.coronawarn.server.common.persistence.domain.DiagnosisKey;
import app.coronawarn.server.services.distribution.assembly.component.CryptoProvider;
import app.coronawarn.server.services.distribution.assembly.structure.directory.Directory;
import app.coronawarn.server.services.distribution.assembly.structure.directory.DirectoryImpl;
import app.coronawarn.server.services.distribution.assembly.structure.directory.IndexDirectory;
import app.coronawarn.server.services.distribution.assembly.structure.directory.decorator.IndexingDecorator;
import app.coronawarn.server.services.distribution.assembly.structure.util.ImmutableStack;
import java.util.Collection;

/**
 * A {@link Directory} containing the file and directory structure that mirrors the API defined in the OpenAPI
 * definition {@code /services/distribution/api_v1.json}. Available countries (endpoint {@code
 * /version/v1/diagnosis-keys/country}) are statically set to only {@code "DE"}. The dates and respective hours
 * (endpoint {@code /version/v1/diagnosis-keys/country/DE/date}) will be created based on the actual {@link DiagnosisKey
 * DiagnosisKeys} given to the {@link DiagnosisKeysDirectoryImpl#DiagnosisKeysDirectoryImpl constructor}.
 */
public class DiagnosisKeysDirectoryImpl extends DirectoryImpl {

  private static final String DIAGNOSIS_KEYS_DIRECTORY = "diagnosis-keys";
  private final Collection<DiagnosisKey> diagnosisKeys;
  private final CryptoProvider cryptoProvider;

  /**
   * Constructs a {@link DiagnosisKeysDirectoryImpl} based on the specified {@link DiagnosisKey} collection.
   * Cryptographic signing is performed using the specified {@link CryptoProvider}.
   *
   * @param diagnosisKeys  The diagnosis keys processed in the contained sub directories.
   * @param cryptoProvider The {@link CryptoProvider} used for payload signing.
   */
  public DiagnosisKeysDirectoryImpl(Collection<DiagnosisKey> diagnosisKeys, CryptoProvider cryptoProvider) {
    super(DIAGNOSIS_KEYS_DIRECTORY);
    this.diagnosisKeys = diagnosisKeys;
    this.cryptoProvider = cryptoProvider;
  }

  @Override
  public void prepare(ImmutableStack<Object> indices) {
    this.addDirectory(decorateCountryDirectory(
        new DiagnosisKeysCountryDirectoryImpl(diagnosisKeys, cryptoProvider)));
    super.prepare(indices);
  }

  private Directory decorateCountryDirectory(IndexDirectory<String> countryDirectory) {
    return new IndexingDecorator<>(countryDirectory);
  }
}