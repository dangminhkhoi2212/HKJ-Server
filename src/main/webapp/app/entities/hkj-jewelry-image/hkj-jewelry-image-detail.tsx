import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-jewelry-image.reducer';

export const HkjJewelryImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjJewelryImageEntity = useAppSelector(state => state.hkjJewelryImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjJewelryImageDetailsHeading">
          <Translate contentKey="serverApp.hkjJewelryImage.detail.title">HkjJewelryImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="serverApp.hkjJewelryImage.url">Url</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.url}</dd>
          <dt>
            <span id="isSearchImage">
              <Translate contentKey="serverApp.hkjJewelryImage.isSearchImage">Is Search Image</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.isSearchImage ? 'true' : 'false'}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="serverApp.hkjJewelryImage.description">Description</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.description}</dd>
          <dt>
            <span id="tags">
              <Translate contentKey="serverApp.hkjJewelryImage.tags">Tags</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.tags}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjJewelryImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjJewelryImage.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjJewelryImageEntity.createdDate ? (
              <TextFormat value={hkjJewelryImageEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjJewelryImage.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjJewelryImageEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjJewelryImage.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjJewelryImageEntity.lastModifiedDate ? (
              <TextFormat value={hkjJewelryImageEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="serverApp.hkjJewelryImage.uploadedBy">Uploaded By</Translate>
          </dt>
          <dd>{hkjJewelryImageEntity.uploadedBy ? hkjJewelryImageEntity.uploadedBy.id : ''}</dd>
          <dt>
            <Translate contentKey="serverApp.hkjJewelryImage.jewelryModel">Jewelry Model</Translate>
          </dt>
          <dd>{hkjJewelryImageEntity.jewelryModel ? hkjJewelryImageEntity.jewelryModel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/hkj-jewelry-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-jewelry-image/${hkjJewelryImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjJewelryImageDetail;
